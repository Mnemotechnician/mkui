package com.github.mnemotechnician.mkui

import arc.scene.ui.*
import arc.scene.ui.layout.*
import arc.scene.style.*
import arc.graphics.*
import arc.graphics.g2d.*
import mindustry.ui.*
import mindustry.gen.*

/** Adds a constant label to the table and returns the created cell */
inline fun Table.addLabel(text: CharSequence, style: Label.LabelStyle = Styles.defaultLabel, wrap: Boolean = false, ellipsis: String? = null): Cell<Label> {
	val label = Label(text, style)
	label.setWrap(wrap)
	label.setEllipsis(ellipsis)
	return add(label)
}

/** Adds a dynamic label to the table and returns the created cell */
inline fun Table.addLabel(crossinline provider: () -> CharSequence, style: Label.LabelStyle = Styles.defaultLabel, wrap: Boolean = true, ellipsis: String? = null): Cell<Label> {
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

inline fun Table.textField(text: String = "", style: TextField.TextFieldStyle = Styles.defaultField, crossinline onchange: TextField.(String) -> Unit = {}): Cell<TextField> {
	val field = TextField(text, style)
	field.changed { field.onchange(field.getText()) }
	return add(field)
}

inline fun Table.textArea(text: String = "", style: TextField.TextFieldStyle = Styles.areaField, crossinline onchange: TextArea.(String) -> Unit = {}): Cell<TextArea> {
	val area = TextArea(text, style)
	area.changed { area.onchange(area.getText()) }
	return add(area)
}

/** Creates a horizontal splitter and returns the created cell. This method automatically creates two rows. */
fun Table.hsplitter(color: Color = Color.white, padding: Float = 5f): Cell<Image> {
	row()
	val cell = addImage(Tex.whiteui)
	row()
	return cell.color(color).fillX().padTop(padding).padBottom(padding)
}

/** Creates a vertical splitter and returns the created cell. */
fun Table.vsplitter(color: Color = Color.white, padding: Float = 5f): Cell<Image> {
	val cell = addImage(Tex.whiteui)
	return cell.color(color).fillY().padLeft(padding).padRight(padding)
}