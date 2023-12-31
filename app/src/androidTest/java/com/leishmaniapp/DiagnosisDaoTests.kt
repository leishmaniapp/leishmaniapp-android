package com.leishmaniapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leishmaniapp.entities.Diagnosis
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
class DiagnosisDaoTests {
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
                database.patientDao().upsertPatient(it.patient)
                database.specialistDao().upsertSpecialist(it.specialist)
                database.diagnosisDao().upsertDiagnosis(it.asRoomEntity())
            }


            Assert.assertEquals(
                diagnoses.map { it.asRoomEntity() },
                database.diagnosisDao().allDiagnoses()
            )

            diagnoses.forEach {
                database.diagnosisDao().deleteDiagnosis(it.asRoomEntity())
            }

            Assert.assertTrue(database.diagnosisDao().allDiagnoses().isEmpty())
        }
    }

    @Test
    fun obtainDiagnosesFromPatient() {

        // Create the mock patient
        val patient = MockGenerator.mockPatient()

        // List of diagnosis with 10 random patients
        val diagnoses: List<Diagnosis> = listOf(
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
                database.specialistDao().upsertSpecialist(diagnosis.specialist)
                database.patientDao().upsertPatient(diagnosis.patient)
                database.diagnosisDao().upsertDiagnosis(diagnosis.asRoomEntity())
            }

            Assert.assertEquals(
                diagnoses.filter { it.patient == patient }.map { it.asRoomEntity() }.toSet(),
                database.diagnosisDao().diagnosesForPatient(patient).toSet()
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
            database.specialistDao().upsertSpecialist(diagnosis.specialist)
            database.patientDao().upsertPatient(diagnosis.patient)
            database.diagnosisDao().upsertDiagnosis(diagnosis.asRoomEntity())
            diagnosis.images.forEach {
                database.imageDao().upsertImage(it.value.asRoomEntity(diagnosis.id))
            }

            // Assert values
            Assert.assertEquals(
                diagnosis.images.values.map { it.asRoomEntity(diagnosis.id) }.toSet(),
                database.diagnosisDao().imagesForDiagnosis(diagnosis.id).images.toSet()
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
            database.specialistDao().upsertSpecialist(specialist)
            database.patientDao().upsertPatient(diagnosis.patient)
            database.diagnosisDao().upsertDiagnosis(diagnosis.asRoomEntity())

            Assert.assertEquals(
                diagnosis.asRoomEntity(),
                database.diagnosisDao().diagnosesForSpecialistNotFinished(specialist.username)
                    .first()
            )
        }
    }

    @Test
    fun diagnosesNotFinishedAnalyzed() {

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
                diagnosis.asRoomEntity(),
                database.diagnosisDao().diagnosesNotFinishedAnalyzed().first()
            )
        }
    }

    @Test
    fun diagnosisForId() {

        val diagnosis = MockGenerator.mockDiagnosis(isFinished = false)

        runBlocking(Dispatchers.IO) {
            // Insert values
            database.specialistDao().upsertSpecialist(diagnosis.specialist)
            database.patientDao().upsertPatient(diagnosis.patient)
            database.diagnosisDao().upsertDiagnosis(diagnosis.asRoomEntity())
            diagnosis.images.values.forEach {
                database.imageDao().upsertImage(it.asRoomEntity(diagnosis.id))
            }

            Assert.assertEquals(
                diagnosis.asRoomEntity(),
                database.diagnosisDao().diagnosisForId(diagnosis.id)
            )
        }
    }
}