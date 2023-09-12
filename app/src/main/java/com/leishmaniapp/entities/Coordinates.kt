package com.leishmaniapp.entities

import kotlinx.serialization.Serializable

/**
 * X, Y Coordinate system
 */
@Serializable
data class Coordinates(val x: Int, val y: Int) {
    fun equalsWithinBoundary(other: Coordinates, coordinateOffset: Int): Boolean {
        return (((other.x >= x) && (x + coordinateOffset >= other.x)) ||
                ((other.x <= x) && (x - coordinateOffset <= other.x))) &&
                (((other.y >= y) && (y + coordinateOffset >= other.y)) ||
                        ((other.y <= y) && (y - coordinateOffset <= other.y)))
    }
}
