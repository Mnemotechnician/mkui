//
// This file was auto-generated by the generateVectorExtensions gradle task!
//

package com.github.mnemotechnician.mkui.extensions

import arc.math.geom.Vec2
import com.github.mnemotechnician.mkui.extensions.*

/** Creates a new Vec2 that is the result of the operation "this plus other". */
inline operator fun Vec2.plus(other: Vec2): Vec2 = Vec2(this).add(other)

/** Modifies this Vec2 so that its value is the result of the operation "this plus other". */
inline operator fun Vec2.plusAssign(other: Vec2) {
	add(other)
}

/** Creates a new Vec2 that is the result of the operation "this minus other". */
inline operator fun Vec2.minus(other: Vec2): Vec2 = Vec2(this).sub(other)

/** Modifies this Vec2 so that its value is the result of the operation "this minus other". */
inline operator fun Vec2.minusAssign(other: Vec2) {
	sub(other)
}

/** Creates a new Vec2 that is the result of the operation "this times other". */
inline operator fun Vec2.times(other: Vec2): Vec2 = Vec2(this).mul(other)

/** Modifies this Vec2 so that its value is the result of the operation "this times other". */
inline operator fun Vec2.timesAssign(other: Vec2) {
	mul(other)
}

/** Creates a new Vec2 that is the result of the operation "this div other". */
inline operator fun Vec2.div(other: Vec2): Vec2 = Vec2(this).div(other)

/** Modifies this Vec2 so that its value is the result of the operation "this div other". */
inline operator fun Vec2.divAssign(other: Vec2) {
	div(other)
}

/** Creates a new Vec2 that is the result of the operation "this times other". */
inline operator fun Vec2.times(other: Float): Vec2 = Vec2(this).mul(other)

/** Modifies this Vec2 so that its value is the result of the operation "this times other". */
inline operator fun Vec2.timesAssign(other: Float) {
	mul(other)
}