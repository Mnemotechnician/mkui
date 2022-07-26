package com.github.mnemotechnician.mkui.ui.element

import arc.scene.ui.Button
import arc.scene.ui.layout.Cell
import mindustry.ui.Styles

/**
 * Represents a toggleable [Button].
 * @see Button
 *
 * @param toggleableStyle the style of this button. Must support the checked state, otherwise an exception will be thrown.
 */
open class ToggleButton(
	defaultToggled: Boolean = false,
	toggleableStyle: ButtonStyle = Styles.defaultb
) : Button() {
	/**
	 * Whether this toggle is turned on.
	 * Modifying this property invokes the toggle listener.
	 */
	var isEnabled = defaultToggled
		set(value) {
			field = value
			toggleListener?.invoke(value)
		}
	protected var toggleListener: ((Boolean) -> Unit)? = null

	init {
		if (toggleableStyle.checked == null) throw IllegalArgumentException("This style does not support checked state!")
		setStyle(toggleableStyle)
		clicked { toggle() }
	}

	/**
	 * Toggles this button.
	 */
	override fun toggle() {
		isEnabled = !isEnabled
	}

	/**
	 * Sets a toggle listener, which will be called whenever this button is toggled.
	 */
	fun toggled(listener: ((Boolean) -> Unit)?) {
		toggleListener = listener
	}

	override fun act(delta: Float) {
		super.act(delta)
		isChecked = isEnabled
	}

	override fun setStyle(style: ButtonStyle) {
		if (style.checked == null) throw IllegalArgumentException("This style does not support checked state!")

		super.setStyle(style)
	}
}

/** Sets a toggle listener for the underlying toggle button. */
inline fun <T : ToggleButton> Cell<T>.toggled(crossinline listener: T.(Boolean) -> Unit) = apply {
	get().toggled { listener(get(), it) }
}

/** Sets whether the underlying toggle button is toggled on. */
fun <T : ToggleButton> Cell<T>.toggle(state: Boolean) = apply {
	get().isEnabled = state
}
