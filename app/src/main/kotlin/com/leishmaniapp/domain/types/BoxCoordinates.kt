package com.leishmaniapp.domain.types

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Represents a bounding box around an element.\
 * Contains 'x' and 'y' coordinates for the top-left corner of the bounding box and its width (w)
 * and height (h). If the w and h parameters are emtpy, then the box represents a center of mass
 * @immutable Replace by using [BoxCoordinates.copy]
 */
@Parcelize
@Serializable
data class BoxCoordinates(
    val x: Int,
    val y: Int,
    val w: Int = 0,
    val h: Int = 0,
) : Parcelable {
    /**
     * Calculates the euclidean distance to another point
     */
    infix fun distanceTo(other: BoxCoordinates): Double = sqrt(
        abs(x - other.x).toDouble().pow(2.0) +
                abs(y - other.y).toDouble().pow(2.0)
    )

    /**
     * Check if the box is a center of mass (no width and height)
     */
    fun isCenterOfMass(): Boolean = (w == 0) && (h == 0)

    /**
     * Get the center of mass of the box
     */
    @IgnoredOnParcel
    val centerOfMass: Pair<Int, Int> = if (isCenterOfMass()) {
        x to y
    } else {
        (x + (w / 2)) to (y + (h / 2))
    }
}