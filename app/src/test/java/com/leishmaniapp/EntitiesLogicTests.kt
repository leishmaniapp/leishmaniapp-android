package com.leishmaniapp

import com.leishmaniapp.entities.Coordinates
import org.junit.Assert
import org.junit.Test

class EntitiesLogicTests {
    @Test
    fun coordinatesEuclideanDistance() {
        val coordinates1 = Coordinates(30, 20)
        val coordinates2 = Coordinates(10, 20)

        Assert.assertEquals(
            coordinates1.distanceTo(coordinates2),
            20.0f
        )

        Assert.assertEquals(
            coordinates2.distanceTo(coordinates1),
            20.0f
        )
    }
}