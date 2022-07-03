package com.github.mnemotechnician.mkui.extensions.groups

import arc.Core
import arc.scene.Element
import arc.scene.Group
import arc.scene.ui.Label
import arc.scene.ui.layout.Table
import arc.scene.ui.layout.WidgetGroup
import com.github.mnemotechnician.mkui.extensions.elements.shrink

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


/**
 * Reduces the size of the whole group to (0, 0) and invalidates hierarchy,
 * effectively reducing the size of this group to the minimum.
 *
 * This method recursively shrinks all children of the group except for [Label]s.
 * That can cause custom [WidgetGroup]s to break if their elements are
 * arranged manually (e.g. via [Element.setSize]).
 *
 * @param invalidateParents whether to invalidate hierarchy. Has no special effect if [shrinkParents] is true.
 * @param shrinkParents whether to shrink all parents except the scene root. Use with caution.
 */
fun Group.deepShrink(shrinkParents: Boolean = false, invalidateParents: Boolean = true) {
	shrink(false)

	for (child in children) {
		if (child is Label) continue
		if (child is Group) {
			child.deepShrink(false, false)
		} else {
			child.shrink(false)
		}
	}

	if (shrinkParents) {
		var group = parent
		while (group != null && group != Core.scene.root) {
			group.shrink()
			group = group.parent
		}
	} else if (invalidateParents) {
		invalidateHierarchy()
	}
}
