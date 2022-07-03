package com.github.mnemotechnician.mkui.delegates

import arc.Core
import arc.util.Strings
import kotlin.reflect.KProperty

/**
 * Creates a property delegate that represents the value of the corresponding mindustry setting.
 *
 * The returned delegate must be used only for one property,
 * using the same setting delegate on multiple properties will result in undefined behaviour.
 *
 * The name of the setting this delegate is bound to is determined by the name of the property,
 * converted to kebab-case, e.g.
 * ```kotlin
 * var isCoolFeatureEnabled by setting(false, "coolMod.")
 * ```
 * delegates to a setting named "coolMod.is-cool-feature-enabled".
 *
 * @param default a default value returned when the setting is absent in the map or its type is different from [T].
 * @param prefix a string prepended to the name of the property.
 */
inline fun <reified T> setting(default: T, prefix: String = ""): SettingDelegate<T> {
	return SettingDelegate(prefix, default)
}

/**
 * Delegates to a setting.
 * @see setting
 */
class SettingDelegate<T>(val prefix: String, val default: T) {
	private var cachedName: String? = null

	operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
		return Core.settings.get(computeName(property), default) as? T ?: default
	}

	operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
		Core.settings.put(computeName(property), value)
	}

	private fun computeName(property: KProperty<*>) = cachedName ?:
		"$prefix${Strings.camelToKebab(property.name)}".also { cachedName = it }
}
