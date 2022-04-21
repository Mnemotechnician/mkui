package com.github.mnemotechnician.mkui.windows

import kotlin.math.*
import arc.*
import arc.math.*
import arc.util.*
import arc.struct.*
import arc.input.*
import arc.graphics.*
import arc.graphics.g2d.*
import arc.scene.*
import arc.scene.actions.*
import arc.scene.event.*
import arc.scene.ui.*
import arc.scene.ui.layout.*
import mindustry.*
import mindustry.ui.*
import mindustry.gen.*
import mindustry.game.*
import com.github.mnemotechnician.mkui.*

/** Manages windows displayed on the screen. A mod should access this object before the ClientLoadEvent gets fired. */
object WindowManager {
	
	internal val windowGroup = WidgetGroup()
	internal val windows = Seq<Window>()
	
	/** Windows that were requested to be created before the window manager was initialized are placed into this queue */
	internal val windowQueue = Queue<Window>(10)
	private var initialized = false
	
	init {
		// TODO: this will probably not be called if the mod hasn't accessed the manager before the game was loaded
		Events.run(EventType.ClientLoadEvent::class.java) {
			windowGroup.setFillParent(true)
			windowGroup.touchable = Touchable.childrenOnly;
			Core.scene.add(windowGroup)
			
			initialized = true
			repeat(windowQueue.size) { constructWindow(windowQueue.removeFirst()) }
			
			Log.info("[blue]Initialized the window manager")
		}
		
		Events.run(EventType.Trigger.update) {
			windows.each {
				// keep in stage
				val root = it.rootTable
				val pos = root.localToParentCoordinates(Tmp.v1.set(0f, 0f));
				
				root.setPosition(
					Mathf.clamp(pos.x, 0f, windowGroup.width - root.getPrefWidth()),
					Mathf.clamp(pos.y, 0f, windowGroup.height - root.getPrefHeight()),
				);
				
				root.color.a = if (it.isDragging) 0.5f else 1f
				root.setSize(root.prefWidth, root.prefHeight)
				
				it.onUpdate()
			}
		}
	}
	
	/** Actually creates the window without any delays. Must be called after the initialization. */
	internal fun constructWindow(window: Window) {
		val windowTable = createWrapper(
			width = { if (window.fullScreen) windowGroup.width else it },
			height = { if (window.fullScreen) windowGroup.height else it }
		) {
			setBackground(Styles.black6)

			lateinit var collapser: Collapser
			
			window.rootTable = this
			
			setClip(true)
			
			// top border
			hsplitter(Color.white, 0f).colspan(3)
			
			// left border
			vsplitter(Color.white, 0f)
			
			// the inner part of the window
			wrapper(
				{ min(it, windowGroup.width) },
				{ min(it, windowGroup.height) }
			) {
				setBackground(Styles.black3)

				// top bar — name, buttons and also a dragger that allows to drag the window
				addTable {
					center().left()

					defaults().height(40f)

					addTable {
						center().left()

						// window name
						addLabel({ window.name }, ellipsis = "...").fillY().growX().get().setFontScale(0.6f)
							
						// adding a drag listener
						addListener(object : InputListener() {
							var dragx = 0f
							var dragy = 0f;
							
							override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: KeyCode): Boolean {
								dragx = x; dragy = y;
								window.isDragging = true;
								return true;
							}
							
							override fun touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {
								val pos = window.rootTable.localToParentCoordinates(Tmp.v1.set(x - dragx, y - dragy))
								window.rootTable.setPosition(pos.x, pos.y)
								
								window.onDrag()

								//the window's been dragged, exit from full screen mode
								if (window.fullScreen) {
									window.fullScreen = false
									window.onFullScreen(false)
								}
							}
							
							override fun touchUp(e: InputEvent, x: Float, y: Float, pointer: Int, button: KeyCode) {
								window.isDragging = false;
							}
						})
					}.growX().fillY()

					// collapse/show button
					textToggle({ if (it) "[accent]=" else "[accent]-" }, Styles.togglet) {
						collapser.setCollapsed(it, true)
						window.isCollapsed = it
						window.onToggle(it)
					}

					// fullscreen button
					hider(hideHorizontal = { !window.supportsFullScreen }) {
						textButton({ if (window.fullScreen) "[accent]•" else "[accent]O" }, Styles.togglet) {
							window.fullScreen = !window.fullScreen
							window.onFullScreen(window.fullScreen)
						}.size(40f).update {
							it.setChecked(window.fullScreen)
						}
					}
					
					// close button
					hider(hideHorizontal = { !window.closeable }) {
						textButton("[red]X", Styles.togglet) {
							this@createWrapper.fadeRemove()
							window.onDestroy()
						}.size(40f)
					}
				}.pad(5f).growX().row()
					
				// main container.
				collapser = addCollapser(true) {
					hsplitter(Color.white, 0f, 5f)
					
					addTable {
						setClip(true)
						setBackground(Styles.black3)
						
						window.table = this
					}.grow()
				}.grow().margin(5f).get()
			}.grow()
			
			// right border
			vsplitter(Color.white, 0f)
			
			// bottom border
			hsplitter(Color.white, 0f).colspan(3)
		}
		
		window.onCreate()
		
		windowGroup.addChild(windowTable)
		windowTable.fadeIn()
		windowTable.setPosition(Core.scene.width / 2, Core.scene.height / 2)
		
		windows.add(window)
	}
	
	/** Constructs & registers the window. If the game hasn't yet loaded, delays the creation */
	fun createWindow(window: Window) {
		if (initialized) {
			constructWindow(window)
		} else {
			windowQueue.add(window)
		}
	}
	
	/** Creates an anonymous window. Such a window won't be able to receive any events other than onCreate */
	inline fun createWindow(name: String, crossinline constructor: Table.() -> Unit) {
		createWindow(object : Window() {
			override var name = name
			
			override var closeable = true
			
			override fun onCreate() {
				table.constructor()
			}
		})
	}
	
	internal fun Element.fadeIn() {
		addAction(Actions.sequence(
			Actions.alpha(0f),
			Actions.fadeIn(0.5f, Interp.pow3)
		))
	}
	
	internal fun Element.fadeRemove() {
		addAction(Actions.sequence(
			Actions.fadeOut(0.5f, Interp.pow3),
			Actions.run { if (parent != null) parent.removeChild(this) }
		))
	}
	
}
