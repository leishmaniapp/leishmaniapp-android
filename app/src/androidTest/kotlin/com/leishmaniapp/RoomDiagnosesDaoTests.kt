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
class RoomDiagnosesDaoTests {
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
    fun createMultipleDiagnosisAndDeleteThem() {

        val diagnoses = List(10) {
            MockGenerator.mockDiagnosis()
        }

        runBlocking(Dispatchers.IO) {
            diagnoses.forEach {
                database.patientsRepository().upsertPatient(it.patient)
                database.specialistsRepository().upsertSpecialist(it.specialist)
                database.diagnosesRepository().upsertDiagnosis(it.asRoomEntity())
            }


            Assert.assertEquals(
                diagnoses.map { it.asRoomEntity() },
                database.diagnosesRepository().allDiagnoses()
            )

            diagnoses.forEach {
                database.diagnosesRepository().deleteDiagnosis(it.asRoomEntity())
            }

            Assert.assertTrue(database.diagnosesRepository().allDiagnoses().isEmpty())
        }
    }

    @Test
    fun obtainDiagnosesFromPatient() {

        // Create the mock patient
        val patient = MockGenerator.mockPatient()

        // List of diagnosis with 10 random patients
        val diagnoses: List<com.leishmaniapp.domain.entities.Diagnosis> = listOf(
            List(10) {
                MockGenerator.mockDiagnosis()
            },
            List(10) {
                MockGenerator.mockDiagnosis().copy(patient = patient)
            }
        ).flatten().toList()

        runBlocking(Dispatchers.IO) {
            // Insert every diagnosis and their specialist and patients
            diagnoses.forEach { diagnosis ->
                database.specialistsRepository().upsertSpecialist(diagnosis.specialist)
                database.patientsRepository().upsertPatient(diagnosis.patient)
                database.diagnosesRepository().upsertDiagnosis(diagnosis.asRoomEntity())
            }

            Assert.assertEquals(
                diagnoses.filter { it.patient == patient }.map { it.asRoomEntity() }.toSet(),
                database.diagnosesRepository().diagnosesForPatient(patient).toSet()
            )
        }
    }

    @Test
    fun diagnosisWithImages() {
        // Generate the diagnosis
        val diagnosis = MockGenerator.mockDiagnosis().copy(images = List(10) {
            MockGenerator.mockImage().copy(sample = it)
        }.associateBy { it.sample })

        runBlocking(Dispatchers.IO) {
            // Insert every diagnosis
            database.specialistsRepository().upsertSpecialist(diagnosis.specialist)
            database.patientsRepository().upsertPatient(diagnosis.patient)
            database.diagnosesRepository().upsertDiagnosis(diagnosis.asRoomEntity())
            diagnosis.images.forEach {
                database.imageSamplesRepository().upsertImage(it.value.asRoomEntity(diagnosis.id))
            }

            // Assert values
            Assert.assertEquals(
                diagnosis.images.values.map { it.asRoomEntity(diagnosis.id) }.toSet(),
                database.diagnosesRepository().imagesForDiagnosis(diagnosis.id).images.toSet()
            )
        }
    }

    @Test
    fun diagnosesForSpecialistNotFinished() {

        val specialist = MockGenerator.mockSpecialist()
        val diagnosis =
            MockGenerator.mockDiagnosis(isFinished = false).copy(specialist = specialist)

        runBlocking(Dispatchers.IO) {
            // Insert values
            database.specialistsRepository().upsertSpecialist(specialist)
            database.patientsRepository().upsertPatient(diagnosis.patient)
            database.diagnosesRepository().upsertDiagnosis(diagnosis.asRoomEntity())

            Assert.assertEquals(
                diagnosis.asRoomEntity(),
                database.diagnosesRepository().diagnosesForSpecialistNotFinished(specialist.email)
                    .first()
            )
        }
    }

    @Test
    fun diagnosesNotFinishedAnalyzed() {

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
                diagnosis.asRoomEntity(),
                database.diagnosesRepository().diagnosesNotFinishedAnalyzed().first()
            )
        }
    }

    @Test
    fun diagnosisForId() {

        val diagnosis = MockGenerator.mockDiagnosis(isFinished = false)

        runBlocking(Dispatchers.IO) {
            // Insert values
            database.specialistsRepository().upsertSpecialist(diagnosis.specialist)
            database.patientsRepository().upsertPatient(diagnosis.patient)
            database.diagnosesRepository().upsertDiagnosis(diagnosis.asRoomEntity())
            diagnosis.images.values.forEach {
                database.imageSamplesRepository().upsertImage(it.asRoomEntity(diagnosis.id))
            }

            Assert.assertEquals(
                diagnosis.asRoomEntity(),
                database.diagnosesRepository().diagnosisForId(diagnosis.id)
            )
        }
    }
}