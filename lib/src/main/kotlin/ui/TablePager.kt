package com.github.mnemotechnician.mkui.ui

import arc.scene.Element
import arc.scene.style.Drawable
import arc.scene.ui.*
import arc.scene.ui.layout.*
import com.github.mnemotechnician.mkui.*
import mindustry.ui.Styles

/**
 * An ui group that displays elements in the form of pages.
 *
 * Consists of a button row that allows the user to switch pages
 * and a page container in which the current page is displayed.
 * New pages can be added via TablePager#addPage.
 *
 * @param vertical if true, the pager will use vertical layout instead of horizontal
 */
open class TablePager(
	val vertical: Boolean = false,
	background: Drawable = Styles.black3
) : Table() {
	/**
	 * All pages added to this pager.
	 * Do not modify manually, as that may break everything.
	 */
	val pages = HashSet<Page>()

	lateinit var buttonsTable: Table
	lateinit var pageContainer: Table
	
	protected val group = ButtonGroup<TextButton>()
	
	init {
		limitedScrollPane(limitH = vertical) {
			margin(5f)
			it.isScrollingDisabledX = vertical
			it.isScrollingDisabledY = !vertical
			
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

		setBackground(background)
	}
	
	/** Sets the background of the two inner tables */
	override fun setBackground(drawable: Drawable) {
		buttonsTable.background = drawable
		pageContainer.background = drawable
	}

	@Deprecated("Backward compatibility method", ReplaceWith("addPage(name, Page())"), DeprecationLevel.ERROR)
	open fun addPage(name: String, page: Table): Cell<TextButton> = addPage(name) { add(page) }.button.cell()!!
	
	/**
	 * Adds a page wrapping the providen element, which fills the table.
	 *
	 * @return the created [Page].
	 */
	fun addPage(pageName: String, element: Element) = addPage(pageName) {
		add(element).grow()
	}

	/**
	 * Adds a page constructed by a lambda.
	 * Note that this method only constructs the page during the function invocation and then reuses it.
	 *
	 * @return the created page.
	 */
	inline fun addPage(pageName: String, constructor: Page.() -> Unit): Page {
		return Page(pageName).apply(constructor)
	}

	/**
	 * Finds a page by name.
	 */
	fun findPage(pageName: String) = pages.find { it.name == pageName }

	/**
	 * Removes a page by name.
	 */
	fun removePage(pageName: String) = findPage(pageName)?.remove() != null

	/**
	 * Removes the providen page. Does nothing if the page doesn't belong to this pager.
	 */
	fun removePage(page: Page) = page in pages && page.remove()

	/**
	 * Represents a page of [TablePager].
	 * Upon the creation, adds a button to [buttonsTable].
	 *
	 * Note that [Page]s of one [TablePager] are incompatible with other.
	 * Moving a page from one pager to another might cause bizarre errors.
	 */
	inner class Page(
		name: String,
		background: Drawable = Styles.none
	) : Table(background) {
		/** The button that allows the user to switch to this page. */
		val button: TextButton
		val parent get() = this@TablePager

		init {
			this.name = name

			button = buttonsTable.textButton({ name }, Styles.togglet) { show() }.also {
				it.group(group)

				if (vertical) {
					it.fillX().row()
				} else {
					it.fillY()
				}
			}.get()

			if (buttonsTable.children.size == 1) button.fireClick()
		}

		/**
		 * Forcibly switches the parent [TablePager] to this page.
		 */
		fun show() {
			pageContainer.clearChildren()
			pageContainer += this

			buttonsTable.invalidateHierarchy()
			pageContainer.invalidateHierarchy()
			pageContainer.pack()
		}

		/**
		 * Removes this page from it's [TablePager].
		 *
		 * @return true if both the button and the page were removed from the parent.
		 */
		override fun remove(): Boolean {
			pages.remove(this)
			return button.remove() && super.remove()
		}
	}
}
