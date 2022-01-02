package com.github.mnemotechnician.mkui.windows

import arc.scene.ui.layout.*
import com.github.mnemotechnician.mkui.*

/** A class that represents a floating on-screen window that the user can drag and interact with. */
abstract class Window {
	
	/** The root of the window */
	lateinit internal var rootTable: Table
	
	/** The table this window is assigned to. Initialized when the window is created. */
	lateinit var table: Table
		internal set
	
	/** Whether this window is collapsed by the user */
	var isCollapsed = false
		internal set
	
	/** Name of this window displayed in the top bar */
	open var name = "unnamed window"
	
	/** Called when the window is being created. At this point the window has a Table assigned to it, which should be inflated by this function. */
	abstract fun onCreate()
	
	/** The window has already been created, this function is called on every tick. It should avoid modifying the table: that can cause a performance loss.  */
	fun onUpdate() {
	}
	
	/** Called whenever the window is being dragged by the user */
	fun onDrag() {
	}
	
	/** Called whenever this window is being toggled by the user */
	fun onToggle(collapsed: Boolean) {
	}
	
}