package com.github.mnemotechnician.mkui

import arc.scene.ui.*
import arc.scene.ui.layout.*
import arc.scene.style.*
import arc.graphics.*
import arc.graphics.g2d.*
import mindustry.ui.*

/** Adds a constant label to the table and returns the created cell */
inline fun Table.addLabel(text: String, style: Label.LabelStyle = Styles.defaultLabel, wrap: Boolean = false, ellipsis: String? = null): Cell<Label> {
	val label = Label(text, style)
	label.setWrap(wrap)
	label.setEllipsis(ellipsis)
	return add(label)
}

/** Adds a dynamic label to the table and returns the created cell */
inline fun Table.addLabel(crossinline provider: () -> String, style: Label.LabelStyle = Styles.defaultLabel, wrap: Boolean = true, ellipsis: String? = null): Cell<Label> {
	val label = Label("", style)
	label.setWrap(wrap)
	label.setEllipsis(ellipsis)
	label.update { label.setText(provider()) }
	return add(label)
}

/** Adds a constant image to the table and returns the created cell */
inline fun Table.addImage(drawable: Drawable): Cell<Image> {
	val i = Image(drawable)
	return add(i)
}

/** Adds a constant image to the table and returns the created cell */
inline fun Table.addImage(drawable: TextureRegion): Cell<Image> {
	val i = Image(drawable)
	return add(i)
}

/** Adds a dynamic image to the table and returns the created cell */
inline fun Table.addImage(crossinline provider: () -> TextureRegion): Cell<Image> {
	val i = Image(provider())
	i.update { provider() }
	return add(i)
}
