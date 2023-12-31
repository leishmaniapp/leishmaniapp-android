package com.leishmaniapp

import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.persistance.entities.DiagnosisRoom.Companion.asRoomEntity
import com.leishmaniapp.persistance.entities.ImageRoom.Companion.asRoomEntity
import com.leishmaniapp.utils.MockGenerator
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.system.measureTimeMillis

/**
 * How many entries to test with
 */
const val NUMBER_OF_ENTRIES = 10000

@RunWith(AndroidJUnit4::class)
class DatabaseLoadTest {

    private lateinit var database: ApplicationDatabase

    @Before
    fun prepareDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), ApplicationDatabase::class.java
        ).build()
    }

    @After
    @Throws(IOException::class)
    fun finalizeDatabase() {
        database.close()
    }

    /**
     * Inserting many fields into database should not take more than 1 second
     */
    @Test
    fun testDatabaseResponseTimeWithManyDiagnosesLessThanASecond() {

        val insertTimes = mutableListOf<Long>()
        val queryTimes = mutableListOf<Long>()
        val diagnoses = mutableListOf<Diagnosis>()

        // Insertion
        for (i in 0..NUMBER_OF_ENTRIES) {
            // Generate the diagnosis
            val diagnosis = MockGenerator.mockDiagnosis()

            // Push new data
            insertTimes.add(measureTimeMillis {
                runBlocking {
                    // Upsert the values
                    database.specialistDao().upsertSpecialist(diagnosis.specialist)
                    database.patientDao().upsertPatient(diagnosis.patient)
                    database.diagnosisDao().upsertDiagnosis(diagnosis.asRoomEntity())
                    diagnosis.images.values.forEach {
                        database.imageDao().upsertImage(it.asRoomEntity(diagnosis.id))
                    }
                }
            })

            diagnoses.add(diagnosis)
        }

        Assert.assertTrue((insertTimes.max() - insertTimes.min()) < 1000)
        Log.d("DatabaseLoadTest", "Insertion: ${insertTimes.min()} <= t <= ${insertTimes.max()}")

        // Querying
        for (diagnosis in diagnoses) {
            queryTimes.add(measureTimeMillis {
                runBlocking {
                    val queryDiagnosis = database.diagnosisDao().diagnosisForId(diagnosis.id)
                    Assert.assertEquals(queryDiagnosis, diagnosis.asRoomEntity())
                }
            })
        }

        Assert.assertTrue((queryTimes.max() - queryTimes.min()) < 1000)
        Log.d("DatabaseLoadTest", "Querying: ${queryTimes.min()} <= t <= ${queryTimes.max()}")
    }

}