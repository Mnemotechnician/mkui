package com.github.mnemotechnician.mkui

import arc.scene.*
import arc.scene.ui.*
import arc.scene.ui.layout.*

/** Adds an element to the Group */
inline operator fun Group.plusAssign(other: Element) { addChild(other) };

/** Adds an element to the Table and returns the created cell */
inline operator fun Table.plusAssign(other: Element) { add(other) }

/** Adds a child to the Group */
inline operator fun Group.plus(other: Element) { this += other }

/** Adds a child to the Table */
inline operator fun Table.plus(other: Element) { this += other }

/** Returns the n-th element of a group or null if it doesn't exist */
inline fun <reified T: Element> Group.childOrNull(index: Int): T? {
	val children = getChildren()
	if (index < 0 || index >= children.size) return null
	return children[index] as? T
}

/** 
 * Returns the n-th child of a group
 * @throws IllegalArgumentException if the element is not an instance of the specified class or doesn't exist
 */
inline fun <reified T: Element> Group.child(index: Int): T = childOrNull<T>(index) ?: throw IllegalArgumentException()

/** Returns the n-th element of a group and automatically casts it to the type parameter */
@Deprecated("Use [child()] instead", level = DeprecationLevel.ERROR)
inline fun <reified T: Element> Group.childAs(index: Int): T = child<T>(index)

/** Returns the n-th element of a group casted to the providen type or null if it doesn't exist / is not an instance of the providen class */
@Deprecated("Use [childOrNull()] instead", level = DeprecationLevel.ERROR)
inline fun <reified T: Element> Group.childAsOrNull(index: Int): T = child<T>(index)
