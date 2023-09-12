package com.leishmaniapp.entities

import kotlinx.serialization.Serializable

/**
 * X, Y Coordinate system
 */
@Serializable
data class Coordinates(val x: Int, val y: Int)