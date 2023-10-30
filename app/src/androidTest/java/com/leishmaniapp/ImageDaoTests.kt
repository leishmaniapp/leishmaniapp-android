package com.leishmaniapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leishmaniapp.entities.ImageAnalysisStatus
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.persistance.entities.DiagnosisRoom.Companion.asRoomEntity
import com.leishmaniapp.persistance.entities.ImageRoom.Companion.asRoomEntity
import com.leishmaniapp.utils.MockGenerator
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

    @Test
    fun testAllImagesForDiagnosis() {
        val images = List(3) {
            MockGenerator.mockImage(processed = ImageAnalysisStatus.NotAnalyzed)
        }

        val diagnosis =
            MockGenerator.mockDiagnosis(isFinished = false)
                .copy(images = images.associateBy { it.sample })

        runBlocking(Dispatchers.IO) {
            // Insert values
            database.specialistDao().upsertSpecialist(diagnosis.specialist)
            database.patientDao().upsertPatient(diagnosis.patient)
            database.diagnosisDao().upsertDiagnosis(diagnosis.asRoomEntity())
            diagnosis.images.values.forEach {
                database.imageDao().upsertImage(it.asRoomEntity(diagnosis.id))
            }

            Assert.assertEquals(
                images.map { it.asRoomEntity(diagnosis.id) }.toSet(),
                database.imageDao().allImagesForDiagnosis(diagnosis.id).toSet()
            )
        }
    }

    @Test
    fun testAllImagesForDiagnosisWithStatus() {
        val images = List(3) {
            MockGenerator.mockImage(processed = ImageAnalysisStatus.Analyzing)
        }

        val diagnosis =
            MockGenerator.mockDiagnosis(isFinished = false)
                .copy(images = images.associateBy { it.sample })

        runBlocking(Dispatchers.IO) {
            // Insert values
            database.specialistDao().upsertSpecialist(diagnosis.specialist)
            database.patientDao().upsertPatient(diagnosis.patient)
            database.diagnosisDao().upsertDiagnosis(diagnosis.asRoomEntity())
            diagnosis.images.values.forEach {
                database.imageDao().upsertImage(it.asRoomEntity(diagnosis.id))
            }

            Assert.assertEquals(
                images.map { it.asRoomEntity(diagnosis.id) }.toSet(),
                database.imageDao()
                    .imagesForDiagnosisWithStatus(diagnosis.id, ImageAnalysisStatus.Analyzing)
                    .toSet()
            )
        }
    }

}
