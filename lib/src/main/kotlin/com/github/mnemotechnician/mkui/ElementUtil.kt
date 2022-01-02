package com.github.mnemotechnician.mkui

import arc.scene.*
import arc.scene.ui.*
import arc.scene.ui.layout.*

/** casts the element to the specified class or returns null if it's not an instance of this class */
inline fun <reified T> Element.asOrNull() = if (this is T) this else null;