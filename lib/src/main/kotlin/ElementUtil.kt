package com.github.mnemotechnician.mkui

import arc.scene.Element
import arc.scene.ui.*
import arc.scene.ui.layout.*
import arc.util.Scaling

/** Text of the label */
inline var Label.content: CharSequence
	get() = text
	set(text) { setText(text) }

/** Text of the label inside of the text button */
inline var TextButton.content: CharSequence
	get() = label.content
	set(text) { label.setText(text) }

/** Text of the text field */
inline var TextField.content: String
	get() = text
	set(text) { setText(text) }

/** Hint of the text field. Displayed when there's no text in the said field. */
inline var TextField.hint: String?
	get() = messageText
	set(hint) { messageText = hint }

/** Casts the element to the specified class or returns null if it's not an instance of this class. */
inline fun <reified T: Element> Element.asOrNull() = this as? T

/** Returns the Cell of this element or null if it's not added to a table */
inline fun <reified T: Element> T.cell(): Cell<T>? = this.parent?.asOrNull<Table>()?.getCell(this) as? Cell<T>?;

/** Returns the element inside a type-erased cell, casted to the providen class */
inline fun <reified T> Cell<Element>.getAs() = get() as T;

/**
 * Returns the element inside a type-erased cell, casting it to the providen class,
 * or null if it's not an instance of this class or if the cell is empty
 */
inline fun <reified T> Cell<Element?>.getAsOrNull(): T? = get()?.let { this as? T }

/** 
 * Changes the font size of the wrapped label or text button and returns the cell 
 * @throws UnsupportedOperationException if the element is neither a Label nor a TextButton
 */
fun Cell<*>.scaleFont(scale: Float) = this.also { cell ->
	when (val it = cell.get()) {
		is Label -> it.setFontScale(scale)
		is TextButton -> it.label?.setFontScale(scale)
		else -> throw UnsupportedOperationException("this class is not supported")
	}
}

/** 
 * Changes Scaling of the wrapped image and returns the cell
 *
 * @throws UnsupportedOperationException if the element is neither an Image nor an ImageButton
 */
fun Cell<*>.scaleImage(scaling: Scaling) = this.also { cell ->
	when (val it = cell.get()) {
		is Image -> it.setScaling(scaling)
		is ImageButton -> it.image?.setScaling(scaling)
		else -> throw UnsupportedOperationException("this class is not supported")
	}
}

