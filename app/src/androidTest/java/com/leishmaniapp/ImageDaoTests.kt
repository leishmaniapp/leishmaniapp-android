package com.leishmaniapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leishmaniapp.entities.DiagnosisRoom.Companion.asRoomEntity
import com.leishmaniapp.entities.ImageRoom.Companion.asRoomEntity
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.persistance.database.ApplicationDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ImageDaoTests {

    private lateinit var database: ApplicationDatabase

    @Before
    fun prepareDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ApplicationDatabase::class.java
        ).build()
    }

    @After
    @Throws(IOException::class)
    fun finalizeDatabase() {
        database.close()
    }

    @Test
    fun insertionAndDeletionTest() {
        // Diagnosis
        val diagnosis = MockGenerator.mockDiagnosis()
        val image = MockGenerator.mockImage().asRoomEntity(diagnosis.id)

        runBlocking(Dispatchers.IO) {
            // Insert the value
            database.patientDao().upsertPatient(diagnosis.patient)
            database.specialistDao().upsertSpecialist(diagnosis.specialist)
            database.diagnosisDao().upsertDiagnosis(diagnosis.asRoomEntity())
            database.imageDao().upsertImage(image)

            // Check if image is present
            Assert.assertEquals(
                image,
                database.imageDao().imageForDiagnosis(diagnosis.id, image.sample)!!
            )

            // Delete the image
            database.imageDao().deleteImage(image)

            // Image must not exist
            Assert.assertNull(database.imageDao().imageForDiagnosis(diagnosis.id, image.sample))
        }
    }

}
