package com.github.mnemotechnician.mkui.windows

import arc.*
import arc.util.*
import arc.struct.*
import arc.scene.event.*
import arc.scene.ui.*
import arc.scene.ui.layout.*
import mindustry.*
import mindustry.ui.*
import mindustry.game.*
import com.github.mnemotechnician.mkui.*

/** Manages all windows displayed on the screen. Lazily initialized. */
object WindowManager {
	
	internal val windowGroup = WidgetGroup()
	val windows = Seq<Window>()
	
	init {
		Events.run(EventType.ClientLoadEvent::class.java) {
			windowGroup.setFillParent(true)
			windowGroup.touchable = Touchable.childrenOnly;
			Core.scene.add(windowGroup)
			
			Log.info("[blue]Initialized the window manager")
		}
		
		Events.run(EventType.Trigger.update) {
			windows.each { it.onUpdate() }
		}
	}
	
	/** Constructs & registers the window */
	fun createWindow(window: Window) {
		val windowTable = Table(Styles.black5).apply {
			//tob bar — name, buttons and also a way to drag the table
			addTable(Styles.black3) {
				addLabel({ window.name }, ellipsis = "...").fillX()
				//collapse/show
				textToggle("-", Styles.togglet) {
					childAs<Label>(0).setText(if (it) "[accent]=" else "[accent]-")
					
					window.isCollapsed = it
					window.onToggle(it)
					
					TODO("collapsing is not yet implemented")
				}
				
				dragged { x, y ->
					window.onDrag()
					TODO("dragging is not yet implemented")
				}
			}.fillX().marginBottom(5f)
			
			row()
			
			//main container
			addTable {
				window.table = this
				window.onCreate()
			}
		}
		
		windowGroup.addChild(windowTable)
		windowTable.setPosition(windowGroup.width / 2, windowGroup.height / 2)
		
		windows.add(window)
	}
	
	/** Creates an anonymous window. Such a window won't be able to receive onUpdate, onToggle, onDrag events. */
	inline fun createWindow(name: String, crossinline constructor: Table.() -> Unit) {
		createWindow(object : Window() {
			override var name = name
			
			override fun onCreate() {
				table.constructor()
			}
		})
	}
	
}