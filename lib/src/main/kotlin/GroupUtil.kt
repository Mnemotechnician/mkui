package com.github.mnemotechnician.mkui

import arc.scene.*
import arc.scene.ui.layout.Table

/**
 * Returns this group with the specified element add to it.
 */
operator fun Group.plus(other: Element) = also { this.addChild(other) }

/**
 * Returns this table with the specified element add to it.
 */
operator fun Table.plus(other: Element) = also { this.add(other) }

/** Returns the n-th element of a group or null if it doesn't exist */
inline fun <reified T: Element> Group.childOrNull(index: Int): T? {
	if (index < 0 || index >= children.size) return null
	return children[index] as? T
}

/** 
 * Returns the n-th child of a group.
 *
 * @throws IllegalArgumentException if the element is not an instance of the specified class or doesn't exist.
 */
inline fun <reified T: Element> Group.child(index: Int): T = childOrNull<T>(index) ?: throw IllegalArgumentException()
