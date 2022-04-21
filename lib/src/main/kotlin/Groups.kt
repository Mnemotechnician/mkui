package com.github.mnemotechnician.mkui

import arc.scene.*
import arc.scene.ui.*
import arc.scene.ui.layout.*
import arc.scene.event.*
import arc.scene.style.*
import arc.struct.*
import arc.graphics.*
import mindustry.ui.*
import com.github.mnemotechnician.mkui.ui.*

/** Creates a table constructed by a lambda */
inline fun createTable(background: Drawable = Styles.none, constructor: Table.() -> Unit = {}): Table {
	return Table(background).apply { constructor() }
}

/** Adds a table constructed by a lambda to the group, passes it to the lamda and returns the created table. */
inline fun Group.addTable(background: Drawable = Styles.none, constructor: Table.() -> Unit = {}): Table {
	return if (this is Table) {
		this.addTable(background, constructor).get() //tables work differently
	} else {
		Table(background).also {
			it.constructor()
			addChild(it)
		}
	}
}

/** Adds a table constructed by a lambda to the existing table and returns the created table cell */
inline fun Table.addTable(background: Drawable = Styles.none, constructor: Table.() -> Unit = {}): Cell<Table> {
	return add(Table(background).also {
		it.constructor()
	})
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
 * Such a scroll pane will not expand in the specified directions unless it's explicitly told to. 
 * This allows to limit the viewport without hardcoding the size.
 */
inline fun Table.limitedScrollPane(limitW: Boolean = true, limitH: Boolean = true, style: ScrollPane.ScrollPaneStyle = Styles.defaultPane, constructor: Table.(ScrollPane) -> Unit): Cell<ScrollPane> {
	val table = Table()
	
	val pane = object : ScrollPane(table, style) {
		override fun getPrefWidth() = if (limitW) width else super.getPrefWidth();
		override fun getPrefHeight() = if (limitH) height else super.getPrefHeight();
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

/** Adds an element stack that contains the specified elements and returns the created cell */
inline fun Table.addStack(vararg elements: Element): Cell<Stack> {
	val stack = Stack()
	elements.forEach { stack += it }
	return add(stack)
}

/** Adds a stack constructed by a lambda and returns the created cell */
inline fun Table.addStack(constructor: Stack.() -> Unit): Cell<Stack> {
	val stack = Stack()
	stack.constructor()
	return add(stack)
}

/** Creates a wrapper table with dynamic size and returns it. */
inline fun createWrapper(
	crossinline width: Table.(originalWidth: Float) -> Float = { it },
	crossinline height: Table.(originalHeight: Float) -> Float = { it },
	crossinline builder: Table.() -> Unit
): Table = object : Table() {
	var lastW = 0f
	var lastH = 0f

	init {
		builder()
		setClip(true)
	}

	override fun act(delta: Float) {
		super.act(delta)

		val newW = getPrefWidth()
		val newH = getPrefHeight()
		if (lastW != newW || lastH != newH) {
			lastW = newW
			lastH = newH
			invalidateHierarchy()
		}

		touchable = if (newW <= 0f || newH <= 0f) Touchable.disabled else Touchable.enabled
	}

	override fun getMinWidth(): Float = getPrefWidth()

	override fun getMinHeight(): Float = getPrefHeight()

	override fun getPrefWidth() = width(super.getPrefWidth())

	override fun getPrefHeight() = height(super.getPrefHeight())
}

/** Adds a wrapper table with dynamic preferred size and returns the created cell. */
inline fun Table.wrapper(
	crossinline width: Table.(originalWidth: Float) -> Float = { it },
	crossinline height: Table.(originalHeight: Float) -> Float = { it },
	crossinline builder: Table.() -> Unit
): Cell<Table> = add(createWrapper(width, height, builder))

/** Adds a table that returns 0 if its preferred width / height if the respective lambda returns true, or it's real preferred width / height otherwise. */
inline fun Table.hider(
	crossinline hideHorizontal: Table.() -> Boolean = { false },
	crossinline hideVertical: Table.() -> Boolean = { false },
	crossinline builder: Table.() -> Unit
) = wrapper(
	width = { if (hideHorizontal()) 0f else it },
	height = { if (hideVertical()) 0f else it },
	builder
)
