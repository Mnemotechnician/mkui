package com.github.mnemotechnician.mkui.windows

import arc.*
import arc.graphics.Color
import arc.input.KeyCode
import arc.math.*
import arc.scene.actions.Actions
import arc.scene.event.*
import arc.scene.ui.layout.*
import arc.struct.*
import arc.util.*
import com.github.mnemotechnician.mkui.*
import mindustry.Vars
import mindustry.game.EventType
import mindustry.ui.Styles
import kotlin.math.min

/**
 * Manages windows displayed on the screen.
 */
object WindowManager {
	internal val windowGroup = WidgetGroup()
	internal val windows = ArrayList<Window>() // not using Seq because it's unstable garbage.

	/** Windows that were requested to be created before the window manager was initialized are placed into this queue */
	internal val windowQueue = Queue<Window>(10)
	private var initialized = false

	init {
		// If client has loaded, this is executed immediately. Otherwise, it is delayed until CLI
		{
			windowGroup.setFillParent(true)
			windowGroup.touchable = Touchable.childrenOnly
			Core.scene.add(windowGroup)

			initialized = true
			repeat(windowQueue.size) { constructWindow(windowQueue.removeFirst()) }

			Log.info("[blue]Initialized the window manager")
		}.let {
			if (Vars.clientLoaded) {
				it()
			} else {
				Events.run(EventType.ClientLoadEvent::class.java, it)
			}
		}

		Events.run(EventType.Trigger.update) {
			windows.forEach {
				// keep in stage
				val root = it.rootTable
				val pos = root.localToParentCoordinates(Tmp.v1.set(0f, 0f))

				root.setPosition(
					Mathf.clamp(pos.x, 0f, windowGroup.width - root.prefWidth),
					Mathf.clamp(pos.y, 0f, windowGroup.height - root.prefHeight),
				)

				root.color.a = if (it.isDragged) 0.5f else 1f
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
			background = Styles.black6
			clip = true

			window.rootTable = this

			// top border
			hsplitter(Color.white, 0f).colspan(3)

			// left border
			vsplitter(Color.white, 0f)

			// the inner part of the window
			wrapper(
				{ min(it, windowGroup.width) },
				{ min(it, windowGroup.height) }
			) {
				background = Styles.black3

				// top bar — name, action buttons. Double as a dragger that enables the user to drag the window
				addTable {
					center().left()

					defaults().height(40f)

					addTable {
						center().left()

						// window name
						addLabel({ window.name }, ellipsis = "...").fillY().growX().get()
							.setFontScale(0.6f)

						// adding a drag listener
						addListener(object : InputListener() {
							var dragx = 0f
							var dragy = 0f

							override fun touchDown(
								event: InputEvent,
								x: Float,
								y: Float,
								pointer: Int,
								button: KeyCode
							): Boolean {
								dragx = x; dragy = y
								window.isDragged = true
								return true
							}

							override fun touchDragged(
								event: InputEvent,
								x: Float,
								y: Float,
								pointer: Int
							) {
								val pos =
									window.rootTable.localToParentCoordinates(
										Tmp.v1.set(
											x - dragx,
											y - dragy
										)
									)
								window.rootTable.setPosition(pos.x, pos.y)

								window.onDrag()

								//the window's been dragged, exit from full screen mode
								if (window.fullScreen) {
									window.fullScreen = false
									window.onFullScreen(false)
								}
							}

							override fun touchUp(
								e: InputEvent,
								x: Float,
								y: Float,
								pointer: Int,
								button: KeyCode
							) {
								window.isDragged = false
							}
						})
					}.growX().fillY()

					// collapse/show button
					textToggle({ if (it) "[accent]=" else "[accent]-" }, Styles.togglet) {
						window.isCollapsed = it
					}

					// fullscreen button
					hider(hideHorizontal = { !window.supportsFullScreen }) {
						textButton(
							{ if (window.fullScreen) "[accent]•" else "[accent]O" },
							Styles.togglet
						) {
							window.fullScreen = !window.fullScreen
						}.size(40f).update {
							it.isChecked = window.fullScreen
						}
					}

					// close button
					hider(hideHorizontal = { !window.closeable }) {
						textButton("[red]X", Styles.togglet) {
							window.destroy()
						}.size(40f)
					}
				}.pad(5f).growX().row()

				// main container.
				window.collapser = addCollapser(true) {
					hsplitter(Color.white, 0f, 5f)

					addTable {
						clip = true
						background = Styles.black3

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
		windowTable.actions(
			Actions.alpha(0f),
			Actions.fadeIn(0.5f, Interp.pow3)
		)
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

	/**
	 * Creates an anonymous window. Such a window won't be able to receive any events other than onCreate.
	 *
	 * @param name the name of the window, displayed on the top bar.
	 * @param constructor constructs the inner layout of the window.
	 */
	inline fun createWindow(name: String, crossinline constructor: Table.() -> Unit) {
		createWindow(object : Window() {
			override var name = name

			override var closeable = true

			override fun onCreate() {
				table.constructor()
			}
		})
	}
}
