package com.github.mnemotechnician.mkui.windows

import arc.*
import arc.math.*
import arc.util.*
import arc.struct.*
import arc.input.*
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
	internal val windows = Seq<Window>()
	
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
				
				root.color.a = if (it.isDragging) 0.5f else 1f
				root.setSize(root.prefWidth, root.prefHeight)
				
				it.onUpdate()
			}
		}
	}
	
	/** Constructs & registers the window */
	fun createWindow(window: Window) {
		val windowTable = Table(Styles.black6).apply {
			lateinit var collapser: Collapser
			
			window.rootTable = this
			
			//top bar — name, buttons and also a way to drag the table
			addTable(Styles.black3) {
				addLabel({ window.name }, ellipsis = "...").growX()
				
				//collapse/show
				textToggle("[accent]-", Styles.togglet) {
					childAs<Label>(0).setText(if (it) "[accent]=" else "[accent]-")
					
					collapser.setCollapsed(it, true)
					window.isCollapsed = it
					window.onToggle(it)
				}.size(50f)
				
				//making it draggable
				addListener(object : InputListener() {
					var dragx = 0f
					var dragy = 0f;
					
					override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: KeyCode): Boolean {
						dragx = x; dragy = y;
						window.isDragging = true;
						return true;
					}
					
					override fun touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {
						val pos = window.rootTable.localToParentCoordinates(Tmp.v1.set(x, y))
						window.rootTable.setPosition(pos.x, pos.y)
						
						window.onDrag()
					}
					
					override fun touchUp(e: InputEvent, x: Float, y: Float, pointer: Int, button: KeyCode) {
						window.isDragging = false;
					}
				})
			}.fillX().marginBottom(5f)
			
			row()
			
			//main container
			collapser = addCollapser(true) {
				setClip(true)
				
				window.table = this
				window.onCreate()
			}.grow().pad(5f).get()
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