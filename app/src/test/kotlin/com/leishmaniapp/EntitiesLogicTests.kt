package com.leishmaniapp

import com.leishmaniapp.domain.types.BoxCoordinates
import org.junit.Assert
import org.junit.Test

class EntitiesLogicTests {
    @Test
    fun coordinatesEuclideanDistance() {
        val boxCoordinates1 = BoxCoordinates(30, 20)
        val boxCoordinates2 = BoxCoordinates(10, 20)

        Assert.assertEquals(
            boxCoordinates1.distanceTo(boxCoordinates2),
            20.0f
        )

        Assert.assertEquals(
            boxCoordinates2.distanceTo(boxCoordinates1),
            20.0f
        )
    }
}