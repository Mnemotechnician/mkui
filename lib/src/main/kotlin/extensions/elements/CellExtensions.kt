package com.github.mnemotechnician.mkui.extensions.elements

import arc.scene.Element
import arc.scene.ui.layout.Cell
import arc.scene.ui.layout.Table

// private val cellColumnField = Cell::class.java.getDeclaredField("column").also { it.isAccessible = true }
// private val cellRowField = Cell::class.java.getDeclaredField("column").also { it.isAccessible = true }
// private val endRowField = Cell::class.java.getDeclaredField("endRow").also { it.isAccessible = true }
//
// var Cell<*>.column
// 	get() = cellColumnField.getInt(this)
// 	set(value) { cellColumnField.setInt(this, value) }
// var Cell<*>.row
// 	get() = cellRowField.getInt(this)
// 	set(value) { cellRowField.setInt(this, value) }
// /** Whether this cell concludes its row. If true, the next cell is placed on a new row. */
// var Cell<*>.endRow: Boolean
// 	get() = endRowField.getBoolean(this)
// 	set(value) { endRowField.setBoolean(this, value) }

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

// /**
//  * Moves the cell of the table by [n] positions and returns it.
//  * If [n] is positive, the cell is moved forward, otherwise backwards.
//  *
//  * The position change can cause the element to move to a different row.
//  *
//  * @param upperRow if true and the cell before the new position concludes the row,
//  * the row end is moved to allow the moved cell to occupy the end of the upper row,
//  * otherwise it will occupy the beginning of the lower row.
//  */
// fun <T : Element> Cell<T>.moveCell(n: Int, upperRow: Boolean = true) = apply {
// 	if (n == 0) return@apply
//
// 	val cells = table.cells
// 	val index = cells.indexOf(this)
// 	val otherIndex = index + n

// 	if (index >= 0 && otherIndex >= 0 && index < cells.size && otherIndex <= cells.size) {
// 		cells.remove(index)
// 		cells.insert(otherIndex, this)
// 		// if one of the cells ends its row, swap the endRow values reflectively
// 		val other = cells[index]
// 		if (endRow != other.endRow) {
// 			val otherEndsRow = other.endRow
// 			val otherColumn = other.column
// 			val otherRow = other.row

// 			other.endRow = endRow
// 			endRow = otherEndsRow
// 			column = otherColumn
// 			row = otherRow

// 			table.invalidate()
// 		}
// 	}

// 	// this will not work
// 	if (otherIndex >= 1 && upperRow) {
// 		val prev = cells[otherIndex - 1]
// 		if (prev.endRow) {
// 			prev.endRow = false
// 			endRow = true
// 			column = other.column + 1
// 		}
// 	}
// }
