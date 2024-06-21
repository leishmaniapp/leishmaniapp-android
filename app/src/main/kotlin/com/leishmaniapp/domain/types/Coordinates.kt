package com.leishmaniapp.domain.types

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
@Parcelize
@Serializable
data class Coordinates(val x: Int, val y: Int) : Parcelable {
    /**
     * Calculates the euclidean distance to another point
     */
    infix fun distanceTo(other: Coordinates): Double = sqrt(
        abs(x - other.x).toDouble().pow(2.0) +
                abs(y - other.y).toDouble().pow(2.0)
    )
}