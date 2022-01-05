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

inline fun Table.scrollPane(style: ScrollPane.ScrollPaneStyle = Styles.defaultPane, element: Element): Cell<ScrollPane> {
	return add(ScrollPane(element, style))
}

/** Creates a scroll pane containing a table constructed by a lambda and returns the created cell */
inline fun Table.scrollPane(style: ScrollPane.ScrollPaneStyle = Styles.defaultPane, constructor: Table.(ScrollPane) -> Unit): Cell<ScrollPane> {
	val table = Table()
	val pane = ScrollPane(table, style)
	table.constructor(pane)
	return add(pane)
}