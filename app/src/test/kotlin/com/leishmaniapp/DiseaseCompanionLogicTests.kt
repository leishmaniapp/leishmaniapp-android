package com.leishmaniapp


import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.disease.LeishmaniasisGiemsaDisease
import com.leishmaniapp.domain.disease.MockSpotsDisease
import org.junit.Assert
import org.junit.Test

class DiseaseCompanionLogicTests {

    @Test
    fun getAllDiseasesMustGiveAllObjectClasses() {
        Assert.assertEquals(setOf(LeishmaniasisGiemsaDisease, MockSpotsDisease), Disease.diseases())
    }

    @Test
    fun getLeishmaniasisGiemsaDiseaseById() {
        Assert.assertEquals(LeishmaniasisGiemsaDisease, Disease.diseaseById("leishmaniasis.giemsa"))
    }
}