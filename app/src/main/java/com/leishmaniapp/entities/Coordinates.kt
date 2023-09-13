package com.leishmaniapp.entities

import kotlinx.serialization.Serializable

/**
 * X, Y Coordinate system in Pixels
 */
@Serializable
data class Coordinates(val x: Int, val y: Int)