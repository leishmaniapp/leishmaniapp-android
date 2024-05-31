package com.leishmaniapp.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * X, Y Coordinate system in Pixels
 * @immutable Replace by using [Coordinates.copy]
 */
@Serializable
@Parcelize
data class Coordinates(val x: Int, val y: Int) : Parcelable {
    infix fun distanceTo(other: Coordinates): Float = sqrt(
        abs(x - other.x).toFloat().pow(2f) +
                abs(y - other.y).toFloat().pow(2f)
    )
}