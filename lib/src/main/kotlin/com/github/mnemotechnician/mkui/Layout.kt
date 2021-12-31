package com.github.mnemotechnician.mkui

import arc.scene.*
import arc.scene.ui.*
import arc.scene.ui.layout.*
import arc.scene.style.*
import arc.graphics.*
import mindustry.ui.*

/** Adds a table to the group, passes it to the lamda and returns the created table. */
inline fun Group.addTable(background: Drawable = Styles.none, constructor: Table.() -> Unit = {}): Table {
	return if (this is Table) {
		this.addTable(background, constructor).get() //tables work differently
	} else {
		val t = Table(background)
		t.constructor()
		addChild(t)
		t
	}
}

/** Adds a table to the existing table and returns the created table cell */
inline fun Table.addTable(background: Drawable = Styles.none, constructor: Table.() -> Unit = {}): Cell<Table> {
	val t = Table(background)
	t.constructor()
	return add(t)
}

/** Adds a constant label to the table and returns the created cell */
inline fun Table.label(text: String, style: Label.LabelStyle = Styles.defaultLabel, wrap: Boolean = false): Cell<Label> {
	val label = Label(text, style)
	label.setWrap(wrap)
	return add(label)
}

/** Adds a dynamic label to the table and returns the created cell */
inline fun Table.label(crossinline provider: () -> String, style: Label.LabelStyle = Styles.defaultLabel, wrap: Boolean = true): Cell<Label> {
	val label = Label("", style)
	label.setWrap(wrap)
	label.update { label.setText(provider()) }
	return add(label)
}

/** Adds a custom button constructed by a lambda and returns the created cell */
inline fun Table.customButton(style: Button.ButtonStyle = Styles.defaultb, crossinline onclick: Button.() -> Unit, constructor: Button.() -> Unit): Cell<Button> {
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

/** Adds an image button with a dynamic image and an optional onclick listener, returns the created cell */
inline fun Table.imageButton(crossinline provider: () -> Drawable, style: ImageButton.ImageButtonStyle = Styles.defaulti, crossinline onclick: ImageButton.() -> Unit = {}): Cell<ImageButton> {
	val b = ImageButton(provider(), style)
	b.update { b.image.setDrawable(provider()) }
	b.clicked { b.onclick() }
	return add(b)
}

/** Adds a constant image to the table and returns the created cell */
inline fun Table.addImage(drawable: Drawable, style: Image.ImageStyle = Styles.defaulti) {
	val i = Image(drawable, style)
	return add(i)
}

/** Adds a dynamic image to the table and returns the created cell */
inline fun Table.addImage(crossinline provider: () -> Drawable, style: Image.ImageStyle = Styles.defaulti) {
	val i = Image(provider(), style)
	i.update { provider() }
	return add(i)
}