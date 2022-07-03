package com.github.mnemotechnician.mkui.extensions.dsl

import arc.graphics.g2d.TextureRegion
import arc.scene.style.Drawable
import arc.scene.ui.*
import arc.scene.ui.layout.*
import arc.util.Scaling
import com.github.mnemotechnician.mkui.extensions.groups.child
import com.github.mnemotechnician.mkui.ui.element.ToggleButton
import mindustry.ui.Styles

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
	crossinline onclick: TextButton.() -> Unit = {}
): Cell<TextButton> {
	return add(TextButton(text, style).also {
		it.clicked { onclick(it) }
		it.label.setWrap(wrap)
	})
}

/** Adds a text button with a dynamic label and an optional onclick listener, returns the created cell */
inline fun Table.textButton(
	crossinline provider: () -> String,
	style: TextButton.TextButtonStyle = Styles.defaultt,
	wrap: Boolean = false,
	crossinline onclick: TextButton.() -> Unit = {}
): Cell<TextButton> {
	return add(TextButton(provider(), style).also {
		it.clicked { onclick(it) }
		it.label.setWrap(wrap)
		it.update { it.setText(provider()) }
	})
}

/** Adds an image button with an optional onclick listener, returns the created cell */
inline fun Table.imageButton(
	image: Drawable,
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
	image: TextureRegion,
	style: ImageButton.ImageButtonStyle = Styles.defaulti,
	crossinline onclick: ImageButton.() -> Unit = {}
): Cell<ImageButton> {
	return add(ImageButton(image, style).also {
		it.clicked { onclick(it) }
	})
}

/** Adds an image button with a dynamic image and an optional onclick listener, returns the created cell */
inline fun Table.imageButton(
	crossinline provider: () -> Drawable,
	style: ImageButton.ImageButtonStyle = Styles.defaulti,
	crossinline onclick: ImageButton.() -> Unit = {}
): Cell<ImageButton> {
	return add(ImageButton(provider(), style).also {
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
	crossinline ontoggle: Button.(Boolean) -> Unit = {}
): Cell<ToggleButton> {
	return toggleButton({ addLabel(text) }, toggleableStyle, ontoggle).also {
		it.get().child<Label>(0).setWrap(wrap)
	}
}

/** Simmilar to toggleButton but adds a dynamic label */
inline fun Table.textToggle(
	crossinline text: (Boolean) -> String,
	toggleableStyle: Button.ButtonStyle = Styles.togglet,
	wrap: Boolean = false,
	crossinline ontoggle: Button.(Boolean) -> Unit = {}
): Cell<ToggleButton> {
	return toggleButton({
		addLabel({ text(isChecked) })
	}, toggleableStyle, ontoggle).also {
		it.get().child<Label>(0).setWrap(wrap)
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
	crossinline ontoggle: Button.(Boolean) -> Unit = {}
): Cell<ToggleButton> {
	return textToggle({ if (it) textEnabled else textDisabled}, toggleableStyle, wrap, ontoggle)
}

/** Simmilar to toggleButton but adds a constant image */
inline fun Table.imageToggle(
	text: Drawable,
	toggleableStyle: Button.ButtonStyle = Styles.clearTogglei,
	crossinline ontoggle: Button.(Boolean) -> Unit = {}
): Cell<ToggleButton> {
	return toggleButton({ addImage(text) }, toggleableStyle, ontoggle)
}

/** Simmilar to toggleButton but adds a dynamic image */
inline fun Table.imageToggle(
	crossinline image: (Boolean) -> Drawable,
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
inline fun Table.textToggle(
	imageEnabled: Drawable,
	imageDisabled: Drawable,
	toggleableStyle: Button.ButtonStyle = Styles.togglet,
	crossinline ontoggle: Button.(Boolean) -> Unit = {}
): Cell<ToggleButton> {
	return imageToggle({ if (it) imageEnabled else imageDisabled}, toggleableStyle, ontoggle)
}
