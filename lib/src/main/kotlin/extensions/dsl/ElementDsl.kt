package com.github.mnemotechnician.mkui.extensions.dsl

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
	alignment: Int = Align.center
): Cell<Label> {
	return add(Label(text, style).also {
		it.setWrap(wrap)
		it.setEllipsis(ellipsis)
		it.setAlignment(alignment)
	})
}

/** Adds a dynamic label to the table and returns the created cell */
inline fun Table.addLabel(
	crossinline provider: () -> CharSequence,
	style: Label.LabelStyle = Styles.defaultLabel,
	wrap: Boolean = true,
	ellipsis: String? = null,
	alignment: Int = Align.center
): Cell<Label> {
	return add(Label("", style).also {
		it.setWrap(wrap)
		it.setEllipsis(ellipsis)
		it.setAlignment(alignment)
		it.update { it.setText(provider()) }
	})
}

/** Adds a constant image to the table and returns the created cell */
fun Table.addImage(drawable: Drawable, scaling: Scaling = Scaling.stretch): Cell<Image> {
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
inline fun Table.addImage(crossinline provider: () -> Drawable, scaling: Scaling = Scaling.stretch): Cell<Image> {
	return add(Image(provider()).also {
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
 * Adds a simple [Element] that returns the providen size as its preferred size.
 */
fun Table.addSpace(
	spaceWidth: Float = 1f,
	spaceHeight: Float = 1f
) = object : Element() {
	override fun getPrefWidth() = spaceWidth
	override fun getPrefHeight() = spaceHeight
}
