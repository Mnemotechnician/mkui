package com.github.mnemotechnician.mkui

import arc.util.*
import arc.scene.*
import arc.scene.ui.*
import arc.scene.ui.layout.*

/** Text of the label */
inline var Label.text: CharSequence?
	get() = getText()
	set(text: CharSequence?) { setText(text) }

/** Text of the label inside of the text button */
inline var TextButton.text: CharSequence?
	get() = label?.getText()
	set(text: CharSequence?) { label?.setText(text) }

/** Text of the text field */
inline var TextField.text: String?
	get() = getText()
	set(text: String?) { setText(text) }

/** Hint of the text field. Displayed when there's no text in the said field. */
inline var TextField.hint: String?
	get() = getMessageText()
	set(hint: String?) = setMessageText(hint)

/** Casts the element to the specified class or returns null if it's not an instance of this class. */
inline fun <reified T: Element> Element.asOrNull() = this as? T

/** Returns the Cell of this element, or null if it's not added to a table */
inline fun <reified T: Element> T.cell(): Cell<Element>? = this.parent?.asOrNull<Table>()?.getCell<T>(this);

/** Returns the element inside a type-erased cell, casted to the providen class */
inline fun <reified T> Cell<Element>.getAs() = get() as T;

/** Returns the element inside a type-erased cell, casted to the providen class, or null if it's not an instance of this class or if the cell is empty */
inline fun <reified T> Cell<Element?>.getAsOrNull(): T? = get()?.let { this as? T }

/** 
 * Changes the font size of the wrapped label or text button and returns the cell 
 * @throws UnsupportedOperationException if the element is not a Label nor a TextButton
 */
fun Cell<*>.scaleFont(scale: Float) = get().let {
	when (it) {
		is Label -> it.setFontScale(scale)
		is TextButton -> it.label?.setFontScale(scale)
		else -> throw UnsupportedOperationException("this class is not supported")
	}
}

/** Changes the font size of the wrapped text button and returns the cell */
@Deprecated("use [scaleFont()] instead", level = DeprecationLevel.ERROR)
fun Cell<TextButton>.scaleButtonFont(scale: Float) = scaleFont(scale)

/** 
 * Changes Scaling of the wrapped image and returns the cell
 * @throws UnsupportedOperationException if the element is not an Image nor an ImageButton
 */
fun Cell<*>.scaleImage(scaling: Scaling) = when (val elem = get()) {
	is Image -> elem.setScaling(scaling)
	is ImageButton -> elem.image?.setScaling(scaling)
	else -> throw UnsupportedOperationException("this class is not supported")
}

/** Sets scaling of the image inside the image button and returns the cell */
@Deprecated("use [scaleImage()] instead", level = DeprecationLevel.ERROR)
fun Cell<ImageButton>.scaleButtonImage(scaling: Scaling) = scaleImage(scaling)
