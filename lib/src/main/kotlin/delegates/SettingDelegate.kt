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
 * converted to kebab-case. In addiction, if [prefix] doesn't end with '-' or '.', a '.' is prepended. E.g.
 * ```kotlin
 * var isCoolFeatureEnabled by setting(false, "coolMod")
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
 * Creates a property delegates that delegates to a mindustry setting.
 *
 * Unlike the delegate returned by [setting], this one computes the lazyPrefix lazily:
 * during the first access. If the value returned by [lazyPrefix] is null,
 * the delegate remains uninitialised, returning the default value and ignoring assignments,
 * but calling [lazyPrefix] during each of these operations until it actually returns a value.
 *
 * See the description of [setting()][setting] for more info.
 */
inline fun <reified T> lazyNameSetting(default: T, noinline lazyPrefix: () -> String?): SettingDelegate<T> {
	return LazyNameSettingDelegate(lazyPrefix, default)
}

/**
 * Delegates to a setting.
 * @see setting
 */
open class SettingDelegate<T>(var prefix: String, val default: T) {
	protected open var cachedName: String? = null

	open operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
		return Core.settings.get(computeName(property), default) as? T ?: default
	}

	open operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
		Core.settings.put(computeName(property), value)
	}

	protected open fun computeName(property: KProperty<*>) = cachedName ?: run {
		val separator = if (prefix.isEmpty() || prefix.endsWith(".") || prefix.endsWith("-")) "" else "."

		"$prefix$separator${Strings.camelToKebab(property.name)}".also { cachedName = it }
	}
}

/**
 * Same as [SettingDelegate], but the name is computed lazily (during the first access).
 * If [lazyPrefix] returns null, the default value is returned, and [lazyPrefix] will be called again on next access.
 * @see lazyNameSetting
 */
open class LazyNameSettingDelegate<T>(
	val lazyPrefix: () -> String?,
	default: T
) : SettingDelegate<T>("", default) {
	override fun getValue(thisRef: Any?, property: KProperty<*>): T {
		if (prefix.isEmpty()) lazyPrefix().let {
			if (it == null) return default
			prefix = it
		}
		return super.getValue(thisRef, property)
	}

	override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
		if (prefix.isEmpty()) lazyPrefix().let {
			if (it == null) return
			prefix = it
		}
		super.setValue(thisRef, property, value)
	}
}
