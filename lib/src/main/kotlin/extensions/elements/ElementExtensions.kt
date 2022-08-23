package com.github.mnemotechnician.mkui.extensions.elements

import arc.Core
import arc.scene.Element
import arc.scene.Group
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

/**
 * Reduces the size of the element to (0, 0) and invalidates it,
 * effectively reducing its size to the minimum. Label is an exception:
 * itd width is set to its preferred width due to how labels work.
 * 
 * For groups, see [Element.deepShrink].
 * 
 * @param invalidateParents whether to invalidate hierarchy
 */
fun Element.shrink(invalidateParents: Boolean = false) {
	if (this !is Label) {
		setSize(0f, 0f)
	} else {
		setSize(prefWidth, 0f)
	}

	if (invalidateParents) invalidateHierarchy() else invalidate()
}

/**
 * Finds an element by its type and name. Can return the current element.
 * If [elementName] is null, only the type is accounted for.
 */
inline fun <reified T: Element> Element.findOrNull(elementName: String? = null): T? {
	if (this is T && elementName == name) return this

	if (this !is Group) return null

	// unfortunately, i can't just make an inline function recursive... and local functions inside of inline ones are not supported yet
	lateinit var rec: Group.() -> T?
	rec = rec@ {
		for (child in children) {
			if (child is T && (elementName == null || child.name == elementName)) return@rec child

			if (child is Group) child.rec()?.also { return@rec it }
		}

		return@rec null
	}

	return rec()
}

/**
 * Finds an element by its name and type. Can return the current element.
 * If [elementName] is null, only the type is accounted for.
 * 
 * @throws IllegalArgumentException if there's no such element in the hierarchy.
 */
inline fun <reified T: Element> Element.findElement(elementName: String? = null): T {
	return findOrNull(elementName)
		?: throw IllegalArgumentException("Element with type ${T::class}${if (elementName == null) "" else " and name '$elementName'"} was not found")
}
