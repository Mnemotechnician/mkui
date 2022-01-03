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
	
	/** Whether the window is being dragged by the user right now */
	var isDragging = false
		internal set
	
	/** Name of this window, displayed in the top bar. Should be overriden. */
	abstract var name: String
	
	/** Whether this window can be closed by the user. Should be overriden. */
	abstract var closeable: Boolean
	
	/** Called when the window is being created. At this point the window has a Table assigned to it, which should be inflated by this function. */
	abstract fun onCreate()
	
	/** The window has already been created, this function is called on every tick. You should avoid modifying the table from this function: that can cause a performance loss. */
	open fun onUpdate() {
	}
	
	/** Called whenever the window is being dragged by the user */
	open fun onDrag() {
	}
	
	/** Called whenever this window is being toggled by the user */
	open fun onToggle(collapsed: Boolean) {
	}
	
	/** Called whenever the window is being destroyed */
	open fun onDestroy() {
	}
	
}