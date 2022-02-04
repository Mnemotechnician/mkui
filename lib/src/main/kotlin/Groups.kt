package com.github.mnemotechnician.mkui

import arc.scene.*
import arc.scene.ui.*
import arc.scene.ui.layout.*
import arc.scene.style.*
import arc.struct.*
import arc.graphics.*
import mindustry.ui.*
import com.github.mnemotechnician.mkui.ui.*

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

/** Creates a scroll pane containing the providen element and returns the created cell */
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

/** 
 * Creates a limited scroll pane constructed by a lambda and returns the created cell.
 * Such a scroll pane will not expand at all unless it's explicitly told to. 
 * This allows to limit the viewport without hardcoding the size.
 */
inline fun Table.limitedScrollPane(style: ScrollPane.ScrollPaneStyle = Styles.defaultPane, constructor: Table.(ScrollPane) -> Unit): Cell<ScrollPane> {
	val table = Table()
	
	val pane = object : ScrollPane(table, style) {
		override fun getPrefWidth() = width;
		override fun getPrefHeight() = height;
	}
	
	table.constructor(pane)
	return add(pane)
}

/** Adds a table pager constructed by a lambda and returns the created cell. The constructor should add new pages via TablePager#addPage. */
inline fun Table.pager(vertical: Boolean = false, constructor: TablePager.() -> Unit): Cell<TablePager> {
	val pager = TablePager(vertical)
	pager.constructor()
	return add(pager)
}