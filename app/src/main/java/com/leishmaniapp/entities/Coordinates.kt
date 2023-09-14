package com.leishmaniapp.entities

import kotlinx.serialization.Serializable
import kotlin.math.*

/**
 * X, Y Coordinate system in Pixels
 */
@Serializable
data class Coordinates(val x: Int, val y: Int) {
    infix fun distanceTo(other: Coordinates): Float = sqrt(
        abs(x - other.x).toFloat().pow(2f) +
                abs(y - other.y).toFloat().pow(2f)
    )
}