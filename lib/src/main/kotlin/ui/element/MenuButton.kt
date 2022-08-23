package com.github.mnemotechnician.mkui.ui.element

import arc.Core
import arc.scene.style.Drawable
import arc.scene.style.Style
import arc.scene.ui.*
import arc.scene.ui.TextButton.TextButtonStyle
import com.github.mnemotechnician.mkui.extensions.dsl.*
import mindustry.ui.Styles

/**
 * A button that has both an icon and a text label.
 * Typically used in menus.
 */
class MenuButton(
	icon: Drawable?,
	text: String?,
	style: TextButtonStyle? = null
) : TextButton(text, style) {
	val icon: Image

	init {
		this.icon = addImage(icon).growY().get()
		cells[0].growX() // make the label occupy all the space
		cells.reverse()
	}
}
