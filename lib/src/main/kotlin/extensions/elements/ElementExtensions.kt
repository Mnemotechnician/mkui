package com.github.mnemotechnician.mkui.extensions.elements

import arc.Core
import arc.graphics.g2d.Font
import arc.scene.Element
import arc.scene.Group
import arc.scene.ui.*
import arc.scene.ui.layout.*
import arc.util.Scaling
import mindustry.gen.Groups.label
import java.awt.SystemColor.text

@PublishedApi
internal var updateField = Element::class.java.getField("update")

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

/** Creates a copy of this label's style and changes its font. */
fun Label.setFont(font: Font) {
	style = Label.LabelStyle(style).also {
		it.font = font
	}
}

/** Creates a copy of this field's style and changes its font. */
fun TextField.setFont(font: Font) {
	style = TextField.TextFieldStyle(style).also {
		it.font = font
		it.messageFont = font
	}
}

/** Creates a copy of this button's label's style and changes its font. */
fun TextButton.setFont(font: Font) {
	label.setFont(font)
}

/** Adds a new update listener that invokes after this element's own update listener finishes. Allows chaining listeners. */
inline fun <T : Element> T.updateLast(crossinline action: (T) -> Unit) {
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

/** Adds a new update listener that invokes before this element's update listener finishes. Allows chaining listeners. */
inline fun <T : Element> T.updateFirst(crossinline action: (T) -> Unit) {
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
