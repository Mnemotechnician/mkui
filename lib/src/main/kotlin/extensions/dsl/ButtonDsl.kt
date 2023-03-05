package com.github.mnemotechnician.mkui.extensions.dsl

import arc.graphics.g2d.TextureRegion
import arc.scene.style.Drawable
import arc.scene.ui.*
import arc.scene.ui.layout.*
import arc.util.Align
import arc.util.Scaling
import com.github.mnemotechnician.mkui.extensions.elements.content
import com.github.mnemotechnician.mkui.extensions.groups.child
import com.github.mnemotechnician.mkui.ui.element.MenuButton
import com.github.mnemotechnician.mkui.ui.element.ToggleButton
import mindustry.gen.Groups.label
import mindustry.ui.Styles
import kotlin.text.Typography.ellipsis

/** Adds a custom button constructed by a lambda and returns the created cell */
inline fun Table.customButton(
	constructor: Button.() -> Unit,
	style: Button.ButtonStyle = Styles.defaultb,
	crossinline onclick: Button.() -> Unit = {}
): Cell<Button> {
	return add(Button(style).also {
		it.clicked { onclick(it) }
		it.constructor()
	})
}

/** Adds a text button with an optional onclick listener, returns the created cell */
inline fun Table.textButton(
	text: String,
	style: TextButton.TextButtonStyle = Styles.defaultt,
	wrap: Boolean = false,
	align: Int? = null,
	ellipsis: String? = null,
	crossinline onclick: TextButton.() -> Unit = {}
): Cell<TextButton> {
	return add(TextButton(text, style).also {
		it.clicked { onclick(it) }
		it.label.setWrap(wrap)
		it.label.setEllipsis(ellipsis)
		align?.let { align -> it.label.setAlignment(align) }
	})
}

/** Adds a text button with a dynamic label and an optional onclick listener, returns the created cell */
inline fun Table.textButton(
	crossinline provider: () -> String,
	style: TextButton.TextButtonStyle = Styles.defaultt,
	wrap: Boolean = false,
	align: Int? = null,
	ellipsis: String? = null,
	crossinline onclick: TextButton.() -> Unit = {}
): Cell<TextButton> {
	return add(TextButton("", style).also {
		it.clicked { onclick(it) }
		it.label.setWrap(wrap)
		it.label.setEllipsis(ellipsis)
		align?.let { align -> it.label.setAlignment(align) }
		it.update { it.setText(provider()) }
	})
}

/** Adds an image button with an optional onclick listener, returns the created cell */
inline fun Table.imageButton(
	image: Drawable?,
	style: ImageButton.ImageButtonStyle = Styles.defaulti,
	crossinline onclick: ImageButton.() -> Unit = {}
): Cell<ImageButton> {
	return add(ImageButton(image, style).also {
		it.image.setScaling(Scaling.bounded)
		it.clicked { onclick(it) }
	})
}

/** Adds an image button with an optional onclick listener, returns the created cell */
inline fun Table.imageButton(
	image: TextureRegion?,
	style: ImageButton.ImageButtonStyle = Styles.defaulti,
	crossinline onclick: ImageButton.() -> Unit = {}
): Cell<ImageButton> {
	return add(ImageButton(image, style).also {
		it.clicked { onclick(it) }
	})
}

/** Adds an image button with a dynamic image and an optional onclick listener, returns the created cell. */
inline fun Table.imageButton(
	crossinline provider: () -> Drawable?,
	style: ImageButton.ImageButtonStyle = Styles.defaulti,
	crossinline onclick: ImageButton.() -> Unit = {}
): Cell<ImageButton> {
	return add(DynamicImageButton(style).also {
		it.clicked { onclick(it) }
		it.update { it.image.drawable = provider() }
	})
}

/** 
 * Creates a toggle button constructed by a lambda and returns the created cell.
 * [ontoggle] is called whenever the button is toggled.
 * @throws IllegalArgumentException when the providen style doesn't support checked state 
 */
inline fun Table.toggleButton(
	constructor: Button.() -> Unit,
	toggleableStyle: Button.ButtonStyle = Styles.togglet,
	crossinline ontoggle: Button.(Boolean) -> Unit = {}
): Cell<ToggleButton> {
	return add(ToggleButton(false, toggleableStyle).also {
		constructor(it)
		it.toggled { state -> ontoggle(it, state) }
	})
}

/** Simmilar to toggleButton but adds a constant label */
inline fun Table.textToggle(
	text: String,
	toggleableStyle: Button.ButtonStyle = Styles.togglet,
	wrap: Boolean = false,
	align: Int? = null,
	ellipsis: String? = null,
	crossinline ontoggle: Button.(Boolean) -> Unit = {}
): Cell<ToggleButton> {
	return toggleButton({ addLabel(text) }, toggleableStyle, ontoggle).also {
		val label = it.get().child<Label>(0)
		label.setWrap(wrap)
		label.setEllipsis(ellipsis)
		align?.let { align -> label.setAlignment(align) }
	}
}

/** Simmilar to toggleButton but adds a dynamic label */
inline fun Table.textToggle(
	crossinline text: (Boolean) -> String,
	toggleableStyle: Button.ButtonStyle = Styles.togglet,
	wrap: Boolean = false,
	align: Int? = null,
	ellipsis: String? = null,
	crossinline ontoggle: Button.(Boolean) -> Unit = {}
): Cell<ToggleButton> {
	return toggleButton({
		addLabel({ text(isChecked) })
	}, toggleableStyle, ontoggle).also {
		val label = it.get().child<Label>(0)
		label.setWrap(wrap)
		label.setEllipsis(ellipsis)
		align?.let { align -> label.setAlignment(align) }
	}
}

/**
 * Adds a text toggle button and returns the created cell.
 * The toggle uses [textEnabled] or [textDisabled] depending on whether it's toggled on or off.
 */
inline fun Table.textToggle(
	textEnabled: String,
	textDisabled: String,
	toggleableStyle: Button.ButtonStyle = Styles.togglet,
	wrap: Boolean = false,
	align: Int? = null,
	ellipsis: String? = null,
	crossinline ontoggle: Button.(Boolean) -> Unit = {}
): Cell<ToggleButton> {
	return textToggle({ if (it) textEnabled else textDisabled }, toggleableStyle, wrap, align, ellipsis, ontoggle)
}

/** Simmilar to toggleButton but adds a constant image */
inline fun Table.imageToggle(
	image: Drawable?,
	toggleableStyle: Button.ButtonStyle = Styles.clearTogglei,
	crossinline ontoggle: Button.(Boolean) -> Unit = {}
): Cell<ToggleButton> {
	return toggleButton({ addImage(image) }, toggleableStyle, ontoggle)
}

/** Simmilar to toggleButton but adds a dynamic image */
inline fun Table.imageToggle(
	crossinline image: (Boolean) -> Drawable?,
	toggleableStyle: Button.ButtonStyle = Styles.clearTogglei,
	crossinline ontoggle: Button.(Boolean) -> Unit = {}
): Cell<ToggleButton> {
	return toggleButton({
		addImage({ image(isChecked) })
	}, toggleableStyle, ontoggle)
}

/**
 * Adds an image toggle button and returns the created cell.
 * The toggle uses [imageEnabled] or [imageDisabled] depending on whether it's toggled on or off.
 */
inline fun Table.imageToggle(
	imageEnabled: Drawable?,
	imageDisabled: Drawable?,
	toggleableStyle: Button.ButtonStyle = Styles.togglet,
	crossinline ontoggle: Button.(Boolean) -> Unit = {}
): Cell<ToggleButton> {
	return imageToggle({ if (it) imageEnabled else imageDisabled}, toggleableStyle, ontoggle)
}

/** Adds a [MenuButton] to the table and returns the created cell. */
fun Table.menuButton(
	icon: Drawable?,
	text: String,
	style: TextButton.TextButtonStyle,
	align: Int = Align.center,
	wrap: Boolean = false,
	ellipsis: String? = null
) = add(MenuButton(icon, text, style).apply {
	label.setAlignment(align)
	label.setWrap(wrap)
	label.setEllipsis(ellipsis)
})

/** Adds a dynamic [MenuButton] to the table and returns the created cell. */
fun Table.menuButton(
	icon: MenuButton.() -> Drawable?,
	text: MenuButton.() -> String,
	style: TextButton.TextButtonStyle,
	align: Int = Align.center,
	wrap: Boolean = false,
	ellipsis: String? = null
) = add(MenuButton(null, null, style).apply {
	label.setAlignment(align)
	label.setWrap(wrap)
	label.setEllipsis(ellipsis)

	update {
		label.content = text()
		this.icon.drawable = icon()
	}
})

/** Workarpund: ImageButton tries to change the drawable before drawing, so this class is required to fix that. */
@PublishedApi
internal class DynamicImageButton(style: ImageButton.ImageButtonStyle) : ImageButton(style) {
	override fun updateImage() {}
}
