package com.github.mnemotechnician.mkui.delegates

import arc.Core
import arc.util.I18NBundle
import arc.util.Strings
import kotlin.reflect.KProperty

/**
 * Creates a read-only property delegate that represents the value of the underlying bundle entry.
 *
 * The returned delegate must be used only for one property,
 * using the same bundle delegate on multiple properties will result in undefined behaviour.
 *
 * The name of the bundle this delegate is bound to is determined by the name of the property,
 * converted to kebab-case. In addiction, if [prefix] doesn't end with '-' or '.', a '.' is prepended. E.g.
 * ```kotlin
 * val coolFeatureName by bundle("coolMod")
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
fun bundle(prefix: String = "", vararg substitutions: Any?) = BundleDelegate<Any?>(prefix, substitutions as Array<Any?>)

/**
 * Creates a bundle delegate with no substitutions.
 * @see bundle
 */
fun bundle(prefix: String = "") = BundleDelegate<Nothing>(prefix, null)

/**
 * Creates a read-only property delegate that delegates to a mindustry bundle entry.
 *
 * Whenever the property is accessed, all providen [functions] are invoked.
 * If the property was accessed before and the outputs of the functions are the same as before,
 * a cached value is returned to avoid unnecessary allocations.
 * Otherwise, [Core.bundle.format()][I18NBundle.format] is called.
 *
 * See the description of [bundle()][bundle] for more info.
 */
fun dynamicBundle(prefix: String = "", vararg functions: () -> Any?) = DynamicBundleDelegate(prefix, functions)

/**
 * Delegates to an I18n bundle entry and allows substitutions.
 * @see bundle
 */
open class BundleDelegate<T>(
	var prefix: String,
	val substitutions: Array<T?>?
) {
	protected open var cachedName: String? = null

	open operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
		return if (substitutions != null && substitutions.isNotEmpty()) {
			Core.bundle.format(computeName(property), *substitutions)
		} else {
			Core.bundle.getOrNull(computeName(property)) ?: "<invalid: $cachedName>"
		}
	}

	protected open fun computeName(property: KProperty<*>) = cachedName ?: run {
		val separator = if (prefix.isEmpty() || prefix.endsWith(".") || prefix.endsWith("-")) "" else "."

		"$prefix$separator${Strings.camelToKebab(property.name)}".also { cachedName = it }
	}
}

/**
 * Similar to [BundleDelegate] but substitutions are performed dynamically.
 * The output is cached and only reformatted if the outputs of [functions] have changed.
 * @see dynamicBundle
 */
open class DynamicBundleDelegate(
	prefix: String,
	val functions: Array<out () -> Any?>?
) : BundleDelegate<Any>(prefix, if (functions != null) arrayOfNulls<Any>(functions.size) else null) {
	var cachedOutput: String? = null

	override fun getValue(thisRef: Any?, property: KProperty<*>): String {
		if (functions == null) return super.getValue(thisRef, property)

		var changed = false
		functions.forEachIndexed { i, func ->
			func().let {
				if (it != substitutions!![i]) changed = true
				substitutions[i] = it
			}
		}

		return if (changed || cachedOutput == null) {
			Core.bundle.format(computeName(property), substitutions).also {
				cachedOutput = it
			}
		} else cachedOutput!!
	}
}
