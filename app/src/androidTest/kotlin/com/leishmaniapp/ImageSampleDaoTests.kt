package com.leishmaniapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leishmaniapp.entities.ImageAnalysisStage
import com.leishmaniapp.infrastructure.persistance.ApplicationDatabase
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
class ImageSampleDaoTests {

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
            database.patientsRepository().upsertPatient(diagnosis.patient)
            database.specialistsRepository().upsertSpecialist(diagnosis.specialist)
            database.diagnosesRepository().upsertDiagnosis(diagnosis.asRoomEntity())
            database.imageSamplesRepository().upsertImage(image)

            // Check if image is present
            Assert.assertEquals(
                image,
                database.imageSamplesRepository().imageForDiagnosis(diagnosis.id, image.sample)!!
            )

            // Delete the image
            database.imageSamplesRepository().deleteImage(image)

            // Image must not exist
            Assert.assertNull(database.imageSamplesRepository().imageForDiagnosis(diagnosis.id, image.sample))
        }
    }

    @Test
    fun testAllImagesForDiagnosis() {
        val images = List(3) {
            MockGenerator.mockImage(processed = ImageAnalysisStage.NotAnalyzed)
        }

        val diagnosis =
            MockGenerator.mockDiagnosis(isFinished = false)
                .copy(images = images.associateBy { it.sample })

        runBlocking(Dispatchers.IO) {
            // Insert values
            database.specialistsRepository().upsertSpecialist(diagnosis.specialist)
            database.patientsRepository().upsertPatient(diagnosis.patient)
            database.diagnosesRepository().upsertDiagnosis(diagnosis.asRoomEntity())
            diagnosis.images.values.forEach {
                database.imageSamplesRepository().upsertImage(it.asRoomEntity(diagnosis.id))
            }

            Assert.assertEquals(
                images.map { it.asRoomEntity(diagnosis.id) }.toSet(),
                database.imageSamplesRepository().allImagesForDiagnosis(diagnosis.id).toSet()
            )
        }
    }

    @Test
    fun testAllImagesForDiagnosisWithStatus() {
        val images = List(3) {
            MockGenerator.mockImage(processed = ImageAnalysisStage.Analyzing)
        }

        val diagnosis =
            MockGenerator.mockDiagnosis(isFinished = false)
                .copy(images = images.associateBy { it.sample })

        runBlocking(Dispatchers.IO) {
            // Insert values
            database.specialistsRepository().upsertSpecialist(diagnosis.specialist)
            database.patientsRepository().upsertPatient(diagnosis.patient)
            database.diagnosesRepository().upsertDiagnosis(diagnosis.asRoomEntity())
            diagnosis.images.values.forEach {
                database.imageSamplesRepository().upsertImage(it.asRoomEntity(diagnosis.id))
            }

            Assert.assertEquals(
                images.map { it.asRoomEntity(diagnosis.id) }.toSet(),
                database.imageSamplesRepository()
                    .imagesForDiagnosisWithStage(diagnosis.id, ImageAnalysisStage.Analyzing)
                    .toSet()
            )
        }
    }

}
