package com.leishmaniapp

import com.leishmaniapp.entities.disease.Disease
import com.leishmaniapp.entities.disease.LeishmaniasisGiemsaDisease
import com.leishmaniapp.entities.disease.MockDotsDisease
import org.junit.Assert
import org.junit.Test

class DiseaseCompanionLogicTests {

    @Test
    fun getAllDiseasesMustGiveAllObjectClasses() {
        Assert.assertEquals(setOf(LeishmaniasisGiemsaDisease, MockDotsDisease), Disease.diseases())
    }

    @Test
    fun getLeishmaniasisGiemsaDiseaseById() {
        Assert.assertEquals(LeishmaniasisGiemsaDisease, Disease.diseaseById("leishmaniasis.giemsa"))
    }
}