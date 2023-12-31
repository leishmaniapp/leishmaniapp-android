package com.leishmaniapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leishmaniapp.utils.MockGenerator
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
class PatientDaoTests {

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
    fun patientCreateSearchAndDelete() {
        // Create mock patient
        val patient = MockGenerator.mockPatient()

        runBlocking(Dispatchers.IO) {
            // Insert the patient
            database.patientDao().upsertPatient(patient)

            // Search patient in database
            Assert.assertEquals(
                patient,
                database.patientDao().patientById(patient.id, patient.documentType)
            )

            // Erase the patient
            database.patientDao().deletePatient(patient)

            // Should not exist
            Assert.assertNull(database.patientDao().patientById(patient.id, patient.documentType))
        }
    }

    @Test
    fun patientCreateAndUpdate() {
        // Create mock patient
        var patient = MockGenerator.mockPatient().copy(name = "A")

        runBlocking(Dispatchers.IO) {
            // Insert the patient
            database.patientDao().upsertPatient(patient)

            // Search patient in database
            Assert.assertEquals(
                patient,
                database.patientDao().patientById(patient.id, patient.documentType)
            )

            // Erase the patient
            patient = patient.copy(name = "B")
            database.patientDao().upsertPatient(patient)

            // Should not exist
            Assert.assertEquals(
                patient,
                database.patientDao().patientById(patient.id, patient.documentType)
            )
        }
    }

    @Test
    fun getAllPatients() {

        // Generate all specialists
        val patients = List(10) { MockGenerator.mockPatient() }

        runBlocking(Dispatchers.IO) {

            // Insert all specialists
            patients.forEach {
                database.patientDao().upsertPatient(it)
            }

            // Check specialists
            Assert.assertEquals(patients, database.patientDao().allPatients())
        }
    }

}