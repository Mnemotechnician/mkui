package com.github.mnemotechnician.mkui.extensions.elements

import arc.graphics.g2d.Font
import arc.scene.Element
import arc.scene.actions.Actions.action
import arc.scene.ui.*
import arc.scene.ui.layout.Cell
import arc.scene.ui.layout.Table
import arc.util.Scaling
import com.github.mnemotechnician.mkui.extensions.groups.rowPer

/** Casts the element to the specified class or returns null if it's not an instance of this class. */
inline fun <reified T: Element> Element.asOrNull() = this as? T

/** Returns the Cell of this element or null if it's not added to a table */
inline fun <reified T: Element> T.cell(): Cell<T>? = this.parent?.asOrNull<Table>()?.getCell(this) as? Cell<T>?;

/** Returns the element inside a type-erased cell, casted to the providen class */
inline fun <reified T : Element> Cell<Element>.getAs() = get() as T;

/**
 * Returns the element inside a type-erased cell, casting it to the providen class,
 * or null if it's not an instance of this class or if the cell is empty
 */
inline fun <reified T : Element> Cell<Element?>.getAsOrNull(): T? = get()?.let { this as? T }

/** See [Table.rowPer]. */
fun <T : Element> Cell<T>.rowPer(number: Int) = apply {
	table.rowPer(number)
}

/**
 * Changes the font size of the wrapped label or text button and returns the cell
 * @throws UnsupportedOperationException if the element is neither a Label nor a TextButton
 */
fun <T : Element> Cell<T>.scaleFont(scale: Float) = this.also { cell ->
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
fun <T : Element> Cell<T>.scaleImage(scaling: Scaling) = this.also { cell ->
	when (val it = cell.get()) {
		is Image -> it.setScaling(scaling)
		is ImageButton -> it.image?.setScaling(scaling)
		else -> throw UnsupportedOperationException("this class is not supported")
	}
}

/** Creates a copy of the wrapped label's style and changes its font. */
@JvmName("setFontLabel")
fun <T : Label> Cell<T>.font(font: Font) = also {
	get().setFont(font)
}

/** Creates a copy of the wrapped field's style and changes its font. */
@JvmName("setFontField")
fun <T : TextField> Cell<T>.font(font: Font) = also {
	get().style = TextField.TextFieldStyle(get().style).also {
		it.font = font
		it.messageFont = font
	}
}

/** Creates a copy of the wrapped button's label's style and changes its font. */
@JvmName("setFontButton")
fun <T : TextButton> Cell<T>.font(font: Font) = also {
	get().setFont(font)
}

/** Adds a new update listener that invokes after the wrapped element's own update listener finishes. Allows chaining listeners. */
inline fun <T : Element> Cell<T>.updateLast(crossinline action: (T) -> Unit) {
	get().apply {
		val oldUpdate = updateField.get(this) as Runnable?
		if (oldUpdate != null) {
			update {
				oldUpdate.run()
				action(this)
			}
		} else {
			update { action(this) }
		}
	}
}

/** Adds a new update listener that invokes before the wrapped element's update listener finishes. Allows chaining listeners. */
inline fun <T : Element> Cell<T>.updateFirst(crossinline action: (T) -> Unit) {
	get().apply {
		val oldUpdate = updateField.get(this) as Runnable?
		if (oldUpdate != null) {
			update {
				action(this)
				oldUpdate.run()
			}
		} else {
			update { action(this) }
		}
	}
}
