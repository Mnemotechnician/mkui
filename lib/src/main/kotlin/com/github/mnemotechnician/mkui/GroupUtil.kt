package com.github.mnemotechnician.mkui

import arc.scene.*
import arc.scene.ui.*
import arc.scene.ui.layout.*

/** Returns the n-th child of a group */
inline fun Group.child(index: Int): Element = getChildren()[index];

/** Returns the n-th element of a group or null if it doesn't exist */
inline fun Group.childOrNull(index: Int): Element? {
	val children = getChildren()
	if (index < 0 || index >= children.size) return null
	return children[index]
}

/** Returns the n-th element of a group and automatically casts it to the type parameter */
inline fun <reified T> Group.childAs(index: Int): T = child(index) as T

/** Returns the n-th element of a group casted to the providen type or null if it doesn't exist / is not an instance of the providen class */
inline fun <reified T> Group.childAsOrNull(index: Int): T? {
	val element = childOrNull(index)
	if (element == null || element !is T) return null
	return element
}