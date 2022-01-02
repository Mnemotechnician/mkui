/**
 * Contains some utility layout construction functions
 * Function names were made distinct in order to avoid ambiguity
 */
package com.github.mnemotechnician.mkui

import arc.scene.*
import arc.scene.ui.*
import arc.scene.ui.layout.*
import arc.scene.style.*
import arc.struct.*
import arc.graphics.*
import mindustry.ui.*

private val tmpButtons = Seq<Button>(10); //used by buttonGroup

/** Adds a table constructed by a lambda to the group, passes it to the lamda and returns the created table. */
inline fun Group.addTable(background: Drawable = Styles.none, constructor: Table.() -> Unit = {}): Table {
	return if (this is Table) {
		this.addTable(background, constructor).get() //tables work differently
	} else {
		val t = Table(background)
		t.constructor()
		addChild(t)
		t
	}
}

/** Adds a table constructed by a lambda to the existing table and returns the created table cell */
inline fun Table.addTable(background: Drawable = Styles.none, constructor: Table.() -> Unit = {}): Cell<Table> {
	val t = Table(background)
	t.constructor()
	return add(t)
}

/** Adds a collapser constructed by a lambda to the existing table and returns the created cell */
inline fun Table.addCollapser(shown: Boolean = true, background: Drawable = Styles.none, constructor: Table.() -> Unit = {}): Cell<Collapser> {
	val table = Table(background)
	table.constructor()
	
	val col = Collapser(table, !shown)
	return add(col)
}

/** Adds a collapser constructed by a lambda to the existing table and returns the created cell. Whether it's shown is determined by the lambda. */
inline fun Table.addCollapser(crossinline shown: () -> Boolean = { true }, background: Drawable = Styles.none, animate: Boolean = false, constructor: Table.() -> Unit = {}): Cell<Collapser> {
	val cell = addCollapser(shown(), background, constructor)
	cell.get().setCollapsed(animate) { !shown() }
	return cell;
}

/** Adds a constant label to the table and returns the created cell */
inline fun Table.addLabel(text: String, style: Label.LabelStyle = Styles.defaultLabel, wrap: Boolean = false, ellipsis: String? = null): Cell<Label> {
	val label = Label(text, style)
	label.setWrap(wrap)
	label.setEllipsis(ellipsis)
	return add(label)
}

/** Adds a dynamic label to the table and returns the created cell */
inline fun Table.addLabel(crossinline provider: () -> String, style: Label.LabelStyle = Styles.defaultLabel, wrap: Boolean = true, ellipsis: String? = null): Cell<Label> {
	val label = Label("", style)
	label.setWrap(wrap)
	label.setEllipsis(ellipsis)
	label.update { label.setText(provider()) }
	return add(label)
}

/** Adds a custom button constructed by a lambda and returns the created cell */
inline fun Table.customButton(constructor: Button.() -> Unit, style: Button.ButtonStyle = Styles.defaultb, crossinline onclick: Button.() -> Unit): Cell<Button> {
	val b = Button(style)
	b.clicked { b.onclick() }
	b.constructor()
	return add(b)
}

/** Adds a text button with an optional onclick listener, returns the created cell */
inline fun Table.textButton(text: String, style: TextButton.TextButtonStyle = Styles.defaultt, crossinline onclick: TextButton.() -> Unit = {}): Cell<TextButton> {
	val b = TextButton(text, style)
	b.clicked { b.onclick() }
	return add(b)
}

/** Adds a text button with a dynamic label and an optional onclick listener, returns the created cell */
inline fun Table.textButton(crossinline provider: () -> String, style: TextButton.TextButtonStyle = Styles.defaultt, crossinline onclick: TextButton.() -> Unit = {}): Cell<TextButton> {
	val b = TextButton(provider(), style)
	b.clicked { b.onclick() }
	b.update { b.setText(provider()) }
	return add(b);
}

/** Adds an image button with an optional onclick listener, returns the created cell */
inline fun Table.imageButton(image: Drawable, style: ImageButton.ImageButtonStyle = Styles.defaulti, crossinline onclick: ImageButton.() -> Unit = {}): Cell<ImageButton> {
	val b = ImageButton(image, style)
	b.clicked { b.onclick() }
	return add(b)
}

/** Adds an image button with a dynamic image and an optional onclick listener, returns the created cell */
inline fun Table.imageButton(crossinline provider: () -> Drawable, style: ImageButton.ImageButtonStyle = Styles.defaulti, crossinline onclick: ImageButton.() -> Unit = {}): Cell<ImageButton> {
	val b = ImageButton(provider(), style)
	b.update { b.image.setDrawable(provider()) }
	b.clicked { b.onclick() }
	return add(b)
}

/** Adds a constant image to the table and returns the created cell */
inline fun Table.addImage(drawable: Drawable): Cell<Image> {
	val i = Image(drawable)
	return add(i)
}

/** Adds a dynamic image to the table and returns the created cell */
inline fun Table.addImage(crossinline provider: () -> Drawable): Cell<Image> {
	val i = Image(provider())
	i.update { provider() }
	return add(i)
}

/** Creates a toggle button constructed by a lambda and returns the created cell. Ontoggle is called whenever the button is toggled.
 *  @throws IllegalArgumentException when the providen style doesn't support checked state */
inline fun Table.toggleButton(constructor: Button.() -> Unit, toggleableStyle: Button.ButtonStyle = Styles.togglet, crossinline ontoggle: Button.(Boolean) -> Unit): Cell<Button> {
	if (toggleableStyle.checked == null) throw IllegalArgumentException("This style does not support checked state!")
	
	var toggled = false //funny arc ui stuff
	val cell = customButton(constructor, toggleableStyle) {
		toggled = !toggled
		ontoggle(toggled)
	}
	cell.update { it.setChecked(toggled) }
	return cell;
}

/** Simmilar to toggleButton but adds a constant label */
inline fun Table.textToggle(text: String, toggleableStyle: Button.ButtonStyle = Styles.togglet, crossinline onclick: Button.(Boolean) -> Unit): Cell<Button> {
	return toggleButton({ addLabel(text) }, toggleableStyle, onclick)
}

/** Simmilar to toggleButton but adds a constant image */
inline fun Table.imageToggle(text: Drawable, toggleableStyle: Button.ButtonStyle = Styles.clearTogglei, crossinline onclick: Button.(Boolean) -> Unit): Cell<Button> {
	return toggleButton({ addImage(text) }, toggleableStyle, onclick)
}

/** Creates a table and a button group, calls the constructor and passes this table to it, adds all created buttons into the same group. Adds the table and returns the created cell. */
inline fun Table.buttonGroup(background: Drawable = Styles.none, constructor: Table.(ButtonGroup<Button>) -> Unit): Cell<Table> {
	val group = ButtonGroup<Button>()
	val table = Table(background)
	table.constructor(group)

	//find all buttons and add them to the group
	table.children.each {
		if (it is Button) group.add(it)
	}
	return add(table)
}
