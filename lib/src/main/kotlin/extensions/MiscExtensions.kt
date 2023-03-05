package com.github.mnemotechnician.mkui.extensions

import arc.Core
import mindustry.ctype.UnlockableContent

val UnlockableContent.emojiOrName get() = (if (hasEmoji()) emoji() else localizedName)!!

inline val Boolean.int get() = if (this) 1 else 0
inline val Int.boolean get() = this >= 0

/** Runs the function on the ui thread. Equivalent to `Core.app.post`. */
fun runUi(block: () -> Unit) = Core.app.post(block)
