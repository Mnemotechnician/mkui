package com.github.mnemotechnician.mkui.windows

import arc.scene.ui.layout.*
import com.github.mnemotechnician.mkui.*

/**
 * A class that represents a floating on-screen window that the user can drag and interact with.
 */
abstract class Window {
	
	/** Name of this window, displayed in the top bar. Should be overriden. */
	abstract val name: String
	
	/** Whether this window can be closed by the user. Should be overriden. */
	abstract val closeable: Boolean

	/** 
	 * Whether this window supports full screen mode. Can be overriden.
	 * Currently broken, should not be used.
	 */
	open val supportsFullScreen: Boolean
		get() = false
	
	/** The root of the window. Should not be modified nor accessed outside of WindowManager. */
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
	
	/** Whether the window is in the full screen mode */
	var fullScreen = false
	
	/**
	 * Called when the window is being created.
	 *
	 * At this point the window has a Table assigned to it, which should be inflated by this function.
	 */
	abstract fun onCreate()
	
	/** 
	 * This function is called on every tick after creating the table.
	 *
	 * Avoid modifying the table in this function: that can cause a performance loss.
	 */
	open fun onUpdate() {
	}
	
	/**
	 * Called when the window is being dragged by the user.
	 *
	 * This function is called continuously.
	 */
	open fun onDrag() {
	}
	
	/** Called whenever this window is being toggled by the user */
	open fun onToggle(collapsed: Boolean) {
	}

	/** Called whenever the user switches between full screen and normal mode */
	open fun onFullScreen(fullscreen: Boolean) {
	}
	
	/** Called when the window is being destroyed. Usually this means that the user has closed the window. */
	open fun onDestroy() {
	}
	
}
