package com.github.mnemotechnician.mkui.delegates

import arc.Core
import arc.util.Strings
import kotlin.reflect.KProperty

/**
 * Creates a read-only property delegate that represents the value of the underlying bundle entry.
 *
 * The returned delegate must be used only for one property,
 * using the same bundle delegate on multiple properties will result in undefined behaviour.
 *
 * The name of the bundle this delegate is bound to is determined by the name of the property,
 * converted to kebab-case, e.g.
 * ```kotlin
 * val coolFeatureName by bundle("coolMod.")
 * ```
 * delegates to a bundle entry named "coolMod.cool-feature-name".
 *
 * Additionally, a list of substitution objects can be provided.
 * If the bundle entry fetched by the delegate contains structures like `{1}`, `{2}`,
 * they will be replaced by the respective entries in the list.
 *
 * @param prefix a string prepended to the name of the property.
 * @param substitutions objects to replace curly bracket references with.
 */
fun bundle(prefix: String = "", vararg substitutions: Any) = BundleDelegate(prefix, substitutions)

/**
 * Creates a bundle delegate with no substitutions.
 * @see bundle
 */
fun bundle(prefix: String = "") = BundleDelegate(prefix, null)

/**
 * Delegates to an I18n bundle entry and allows substitutions.
 * @see bundle
 */
class BundleDelegate(
	val prefix: String,
	val substitutions: Array<out Any>?
) {
	private var cachedName: String? = null

	operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
		return if (substitutions != null && substitutions.isNotEmpty()) {
			Core.bundle.format(computeName(property), *substitutions)
		} else {
			Core.bundle[computeName(property)]
		}
	}

	private fun computeName(property: KProperty<*>) = cachedName
		?: "$prefix${Strings.camelToKebab(property.name)}".also { cachedName = it }
}
