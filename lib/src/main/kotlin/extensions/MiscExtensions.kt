package extensions

import mindustry.ctype.UnlockableContent

val UnlockableContent.emojiOrName get() = (if (hasEmoji()) emoji() else localizedName)!!

inline val Boolean.int get() = if (this) 1 else 0
inline val Int.boolean get() = this >= 0
