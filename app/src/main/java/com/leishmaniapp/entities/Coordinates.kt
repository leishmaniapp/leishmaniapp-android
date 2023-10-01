package com.leishmaniapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * X, Y Coordinate system in Pixels
 * @immutable Replace by using [Coordinates.copy]
 */
@Serializable
data class Coordinates(val x: Int, val y: Int) {
    infix fun distanceTo(other: Coordinates): Float = sqrt(
        abs(x - other.x).toFloat().pow(2f) +
                abs(y - other.y).toFloat().pow(2f)
    )
}