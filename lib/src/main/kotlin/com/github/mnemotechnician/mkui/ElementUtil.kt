/** A package containg utility functions for Elements and Cells */
package com.github.mnemotechnician.mkui

import arc.scene.*
import arc.scene.ui.*
import arc.scene.ui.layout.*

/** casts the element to the specified class or returns null if it's not an instance of this class */
inline fun <reified T> Element.asOrNull() = if (this is T) this else null;

/** Returns the element inside a type-erased cell, casted to the providen class */
inline fun <reified T> Cell<Element>.getAs() = get() as T;

/** Returns the element inside a type-erased cell, casted to the providen class, or null if it's not an instance of this class or if the cell is empty */
inline fun <reified T> Cell<Element?>.getAsOrNull() = get()?.let {
	if (this !is T) this else null
};

/** Changes the font size of the wrapped label and returns the cell */
fun Cell<Label>.scaleFont(scale: Float) = this.apply {
	get().setFontScale(scale)
};

/** Changes the font size of the wrapped text button and returns the created cell */
fun Cell<TextButton>.scaleButtonFont(scale: Float) = this.apply {
	get().label.setFontScale(scale)
}