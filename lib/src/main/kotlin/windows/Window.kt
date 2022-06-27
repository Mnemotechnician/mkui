package com.github.mnemotechnician.mkui.windows

import arc.math.Interp
import arc.scene.Action
import arc.scene.actions.Actions
import arc.scene.ui.layout.Collapser
import arc.scene.ui.layout.Table
import com.github.mnemotechnician.mkui.deepShrink

/**
 * A class that represents a floating on-screen window that the user can drag and interact with.
 *
 * In most cases an instance of this class has to be passed to [WindowManager] before anything can be done with it.
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
	
	/** The root of the window. Should not be modified nor accessed outside WindowManager. */
	internal lateinit var rootTable: Table

	/** The collapser that wraps [table].*/
	internal lateinit var collapser: Collapser
	
	/** The table this window is assigned to. Initialized when the window is created. */
	lateinit var table: Table
		internal set

	/** Whether the window is being dragged by the user right now */
	var isDragged = false
		internal set

	/**
	 * Whether this window is collapsed.
	 * Modifying this property triggers [onToggle].
	 */
	var isCollapsed = false
		set(value) {
			collapser.setCollapsed(value, true)
			field = value
			onToggle(value)
		}

	/**
	 * Whether the window is in the full screen mode.
	 * Modifying this property triggers [onFullScreen].
	 */
	var fullScreen = false
		set(value) {
			field = value
			onFullScreen(value)
		}
	
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

	/**
	 * Destroys this window, removing it from the scene and triggering [onDestroy].
	 */
	fun destroy() {
		WindowManager.windows.remove(this)
		addAction(
			Actions.sequence(
				Actions.fadeOut(0.5f, Interp.pow3),
				Actions.run { rootTable.parent?.removeChild(rootTable) }
			)
		)
		onDestroy()
	}

	/**
	 * Shrinks the root table of this window, reducing it's size to the minimum preferred size.
	 */
	open fun shrink() = rootTable.deepShrink()

	/**
	 * Applies an action to the **root table**.
	 */
	open fun addAction(action: Action) = rootTable.addAction(action)

	override fun equals(other: Any?): Boolean {
		return this === other || (other is Window && name == other.name && closeable == other.closeable && rootTable == other.rootTable)
	}

	override fun hashCode(): Int {
		var result = name.hashCode()
		result = 31 * result + closeable.hashCode()
		result = 31 * result + supportsFullScreen.hashCode()
		result = 31 * result + rootTable.hashCode()
		return result
	}
}
