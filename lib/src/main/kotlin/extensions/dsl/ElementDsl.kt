package com.github.mnemotechnician.mkui.extensions.dsl

import arc.func.Prov
import arc.graphics.Color
import arc.graphics.g2d.TextureRegion
import arc.scene.Element
import arc.scene.style.Drawable
import arc.scene.ui.*
import arc.scene.ui.layout.*
import arc.util.*
import com.github.mnemotechnician.mkui.extensions.elements.content
import mindustry.gen.Tex
import mindustry.ui.Styles

/** Adds a constant label to the table and returns the created cell */
fun Table.addLabel(
	text: CharSequence,
	style: Label.LabelStyle = Styles.defaultLabel,
	wrap: Boolean = false,
	ellipsis: String? = null,
	align: Int = Align.center
): Cell<Label> {
	return add(Label(text, style).also {
		it.setWrap(wrap)
		it.setEllipsis(ellipsis)
		it.setAlignment(align)
	})
}

/** Adds a dynamic label to the table and returns the created cell */
inline fun Table.addLabel(
	crossinline provider: () -> CharSequence,
	style: Label.LabelStyle = Styles.defaultLabel,
	wrap: Boolean = false,
	ellipsis: String? = null,
	align: Int = Align.center
): Cell<Label> {
	return add(Label("", style).also {
		it.setWrap(wrap)
		it.setEllipsis(ellipsis)
		it.setAlignment(align)
		it.update { it.setText(provider()) }
	})
}

/**
 * Adds multiple labels, either static or dynamic ones.
 *
 * Each of the elements must be either an arbitrary object,
 * which will be `toString()`ed, or a [() -> Any?] / [Prov].
 * For the latter ones dynamic labels are made.
 *
 * The parameter [block] can be used to modify the created labels, e.g. apply color or padding.
 */
inline fun Table.addLabels(
	vararg elems: Any?,
	style: Label.LabelStyle = Styles.defaultLabel,
	wrap: Boolean = false,
	ellipsis: String? = null,
	align: Int = Align.center,
	block: (Cell<Label>) -> Unit = {}
) {
	elems.forEach {
		when (it) {
			// union types are not yet supported in kotlin, so...
			is () -> Any? -> addLabel({ it().toString() }, style, wrap, ellipsis, align)
			is Prov<out Any?> -> addLabel({ it.get().toString() }, style, wrap, ellipsis, align)
			else -> addLabel(it.toString(), style, wrap, ellipsis, align)
		}.also {
			block(it)
		}
	}
}

/** Adds a constant image to the table and returns the created cell */
fun Table.addImage(drawable: Drawable?, scaling: Scaling = Scaling.stretch): Cell<Image> {
	val i = Image(drawable)
	i.setScaling(scaling)
	return add(i)
}

/** Adds a constant image to the table and returns the created cell */
fun Table.addImage(drawable: TextureRegion, scaling: Scaling = Scaling.stretch): Cell<Image> {
	return add(Image(drawable).also {
		it.setScaling(scaling)
	})
}

/** Adds a dynamic image to the table and returns the created cell */
inline fun Table.addImage(crossinline provider: () -> Drawable?, scaling: Scaling = Scaling.stretch): Cell<Image> {
	return add(Image().also {
		it.setScaling(scaling)
		it.update { it.drawable = provider() }
	})
}

/**
 * Adds a single-line TextField to the table and returns the created cell.
 */
inline fun Table.textField(
	text: String = "",
	style: TextField.TextFieldStyle = Styles.defaultField,
	crossinline onchange: TextField.(String) -> Unit = {}
): Cell<TextField> {
	return add(TextField(text, style).also {
		it.changed { onchange(it, it.content) }
	})
}

/**
 * Adds a multi-line TextArea to the table and returns thencreated cell.
 */
inline fun Table.textArea(
	text: String = "",
	style: TextField.TextFieldStyle = Styles.areaField,
	crossinline onchange: TextArea.(String) -> Unit = {}
): Cell<TextArea> {
	return add(TextArea(text, style).also {
		it.changed { it.onchange(it.text) }
	})
}

/** Creates a horizontal splitter and returns the created cell. This method automatically creates two rows. */
fun Table.hsplitter(color: Color = Color.white, padTop: Float = 5f, padBottom: Float = padTop): Cell<Image> {
	row()
	return addImage(Tex.whiteui).color(color).fillX().padTop(padTop).padBottom(padBottom).also {
		row()
	}
}

/** Creates a vertical splitter and returns the created cell. */
fun Table.vsplitter(color: Color = Color.white, padLeft: Float = 5f, padRight: Float = padLeft): Cell<Image> {
	return addImage(Tex.whiteui).color(color).fillY().padLeft(padLeft).padRight(padRight)
}

/**
 * Adds a simple [Element] that occupies the providen size.
 */
fun Table.addSpace(
	width: Float = 1f,
	height: Float = 1f
) = add(Element()).size(width, height)

/*
 * Adds an [Element] that returns the providen values as its preferred size.
 */
fun Table.addPrefSpace(
	spaceWidth: Float = 1f,
	spaceHeight: Float = 1f
): Cell<Element> = add(object : Element() {
	override fun getPrefWidth() = spaceWidth
	override fun getPrefHeight() = spaceHeight
})
