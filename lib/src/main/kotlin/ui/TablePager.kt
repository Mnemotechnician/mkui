package com.github.mnemotechnician.mkui.ui

import arc.scene.style.Drawable
import arc.scene.ui.*
import arc.scene.ui.layout.*
import com.github.mnemotechnician.mkui.*
import mindustry.ui.Styles

/**
 * A ui group that displays elements in the form of pages.
 *
 * Consists of a button row that allows the user to switch pages and a page container in which the current page is displayed
 * New pages can be added via TablePager#addPage.
 *
 * @param vertical if true, the pager will use vertical layout instead of horizontal
 */
open class TablePager(val vertical: Boolean = false) : Table() {
	
	lateinit var buttonsTable: Table
	lateinit var pageContainer: Table
	
	protected val group = ButtonGroup<TextButton>()
	
	init {
		limitedScrollPane(limitH = vertical) {
			margin(5f)
			it.setScrollBarPositions(!vertical, vertical)
			
			buttonsTable = this
		}.also {
			if (vertical) {
				it.growY().marginBottom(5f)
			} else {
				it.growX().marginRight(5f).row()
			}
		}
		
		addTable {
			margin(5f).defaults().grow()
			
			pageContainer = this
		}.padLeft(5f).grow()

		background = Styles.black3
	}
	
	/** Sets the background of the two inner tables */
	override fun setBackground(drawable: Drawable) {
		buttonsTable.background = drawable
		pageContainer.background = drawable
	}
	
	/** Adds a page and a respective button. @return the cell of the button */
	open fun addPage(name: String, page: Table): Cell<TextButton> {
		return buttonsTable.textButton(name, Styles.togglet) {
			pageContainer.clearChildren()
			pageContainer += page

			buttonsTable.invalidateHierarchy()
			pageContainer.invalidateHierarchy()
		}.also {
			it.group(group)
			
			if (vertical) {
				it.fillX().row()
			} else {
				it.fillY()
			}
			
			if (buttonsTable.children.size <= 1) it.get().fireClick() //the first added button should be clicked automatically
		}
	}
	
	/**
	 * Adds a page constructed by a lambda and a respective button.
	 * Note that this method only constructs the page during the function invocation and then reuses it.
	 *
	 * @return the cell of the created button that opens this page.
	 */
	inline fun addPage(name: String, constructor: Table.() -> Unit): Cell<TextButton> {
		return addPage(name, Table().also {
			it.constructor()
		})
	}
	
}
