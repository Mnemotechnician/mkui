package com.github.mnemotechnician.mkui

import arc.scene.*
import arc.scene.ui.*
import arc.scene.ui.layout.*
import arc.scene.style.*
import arc.struct.*
import arc.graphics.*
import arc.graphics.g2d.*
import mindustry.ui.*

/** Adds a custom button constructed by a lambda and returns the created cell */
inline fun Table.customButton(constructor: Button.() -> Unit, style: Button.ButtonStyle = Styles.defaultb, crossinline onclick: Button.() -> Unit): Cell<Button> {
	val b = Button(style)
	b.clicked { b.onclick() }
	b.constructor()
	return add(b)
}

/** Adds a text button with an optional onclick listener, returns the created cell */
inline fun Table.textButton(text: String, style: TextButton.TextButtonStyle = Styles.defaultt, crossinline onclick: TextButton.() -> Unit = {}): Cell<TextButton> {
	val b = TextButton(text, style)
	b.clicked { b.onclick() }
	return add(b)
}

/** Adds a text button with a dynamic label and an optional onclick listener, returns the created cell */
inline fun Table.textButton(crossinline provider: () -> String, style: TextButton.TextButtonStyle = Styles.defaultt, crossinline onclick: TextButton.() -> Unit = {}): Cell<TextButton> {
	val b = TextButton(provider(), style)
	b.clicked { b.onclick() }
	b.update { b.setText(provider()) }
	return add(b);
}

/** Adds an image button with an optional onclick listener, returns the created cell */
inline fun Table.imageButton(image: Drawable, style: ImageButton.ImageButtonStyle = Styles.defaulti, crossinline onclick: ImageButton.() -> Unit = {}): Cell<ImageButton> {
	val b = ImageButton(image, style)
	b.clicked { b.onclick() }
	return add(b)
}

/** Adds an image button with an optional onclick listener, returns the created cell */
inline fun Table.imageButton(image: TextureRegion, style: ImageButton.ImageButtonStyle = Styles.defaulti, crossinline onclick: ImageButton.() -> Unit = {}): Cell<ImageButton> {
	val b = ImageButton(image, style)
	b.clicked { b.onclick() }
	return add(b)
}

/** Adds an image button with a dynamic image and an optional onclick listener, returns the created cell */
inline fun Table.imageButton(crossinline provider: () -> TextureRegion, style: ImageButton.ImageButtonStyle = Styles.defaulti, crossinline onclick: ImageButton.() -> Unit = {}): Cell<ImageButton> {
	val b = ImageButton(provider(), style)
	b.update { b.image.setDrawable(provider()) }
	b.clicked { b.onclick() }
	return add(b)
}

/** Creates a toggle button constructed by a lambda and returns the created cell. Ontoggle is called whenever the button is toggled.
 *  @throws IllegalArgumentException when the providen style doesn't support checked state */
inline fun Table.toggleButton(constructor: Button.() -> Unit, toggleableStyle: Button.ButtonStyle = Styles.togglet, crossinline ontoggle: Button.(Boolean) -> Unit): Cell<Button> {
	if (toggleableStyle.checked == null) throw IllegalArgumentException("This style does not support checked state!")
	
	var toggled = false //funny arc ui stuff
	val cell = customButton(constructor, toggleableStyle) {
		toggled = !toggled
		ontoggle(toggled)
	}
	cell.update { it.setChecked(toggled) }
	return cell;
}

/** Simmilar to toggleButton but adds a constant label */
inline fun Table.textToggle(text: String, toggleableStyle: Button.ButtonStyle = Styles.togglet, crossinline onclick: Button.(Boolean) -> Unit): Cell<Button> {
	return toggleButton({ addLabel(text) }, toggleableStyle, onclick)
}

/** Simmilar to toggleButton but adds a constant image */
inline fun Table.imageToggle(text: Drawable, toggleableStyle: Button.ButtonStyle = Styles.clearTogglei, crossinline onclick: Button.(Boolean) -> Unit): Cell<Button> {
	return toggleButton({ addImage(text) }, toggleableStyle, onclick)
}