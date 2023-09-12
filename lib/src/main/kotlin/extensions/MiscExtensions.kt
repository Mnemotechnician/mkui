package com.github.mnemotechnician.mkui.extensions

import arc.Core
import arc.graphics.Color
import arc.math.geom.*
import arc.util.Log
import mindustry.ctype.UnlockableContent

val UnlockableContent.emojiOrName get() = (if (hasEmoji()) emoji() else localizedName)!!

inline val Boolean.int get() = if (this) 1 else 0
inline val Int.boolean get() = this >= 0

/** Runs the function on the ui thread. Equivalent to `Core.app.post`. */
fun runUi(block: () -> Unit) = Core.app.post(block)

/** Runs the specified function on the ui thread and catches any exceptions. */
inline fun <reified T : Exception> runUiCatching(
	crossinline onFailure: (T) -> Unit = { Log.err("Failure in runUiCatching", it) },
	crossinline block: () -> Unit
) {
	runUi {
		try {
			block()
		} catch (e: Throwable) {
			if (e !is T) throw e
			onFailure(e)
		}
	}
}

fun Vec2.mul(other: Vec2) = set(x * other.x, y * other.y)
fun Vec2.mul(other: Float) = set(x * other, y * other)

fun Vec3.mul(other: Vec3) = set(x * other.x, y * other.y, z * other.z)
fun Vec3.mul(other: Float) = set(x * other, y * other, z * other)
