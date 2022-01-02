package com.github.mnemotechnician.mkui.windows

import arc.*
import arc.math.*
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
			windows.each {
				//keep in stage
				val root = it.rootTable
				val pos = root.localToParentCoordinates(Tmp.v1.set(0f, 0f));
				
				root.setPosition(
					Mathf.clamp(pos.x, root.getPrefWidth() / 2, windowGroup.width - root.getPrefWidth() / 2),
					Mathf.clamp(pos.y, root.getPrefHeight() / 2, windowGroup.height - root.getPrefHeight() / 2)
				);
				
				it.onUpdate()
			}
		}
	}
	
	/** Constructs & registers the window */
	fun createWindow(window: Window) {
		val windowTable = Table(Styles.black5).apply {
			lateinit var collapser: Collapser
			
			window.rootTable = this
			
			//top bar — name, buttons and also a way to drag the table
			addTable(Styles.black3) {
				addLabel({ window.name }, ellipsis = "...").fillX()
				
				//collapse/show
				textToggle("-", Styles.togglet) {
					childAs<Label>(0).setText(if (it) "[accent]=" else "[accent]-")
					
					collapser.toggle()
					window.isCollapsed = it
					window.onToggle(it)
				}
				
				dragged { x, y ->
					val oldPos = window.rootTable.localToParentCoordinates(Tmp.v1.set(x, y))
					window.rootTable.setPosition(oldPos.x, oldPos.y)
					
					window.onDrag()
				}
			}.fillX().marginBottom(5f)
			
			row()
			
			//main container
			collapser = addCollapser(animate = true) {
				setClip(true)
				
				window.table = this
				window.onCreate()
			}.pad(5f).get()
		}
		
		windowGroup.addChild(windowTable)
		windowTable.setPosition(Core.scene.width / 2, Core.scene.height / 2)
		
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