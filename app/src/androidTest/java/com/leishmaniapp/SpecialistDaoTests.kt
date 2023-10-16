package com.leishmaniapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leishmaniapp.entities.Password
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
class SpecialistDaoTests {

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
    fun specialistCreateSearchAndDelete() {
        // Create mock specialist
        val specialist = MockGenerator.mockSpecialist()

        runBlocking(Dispatchers.IO) {
            // Insert the specialist
            database.specialistDao().upsertSpecialist(specialist)

            // Search specialist in database
            Assert.assertEquals(
                specialist,
                database.specialistDao().specialistByUsername(specialist.username)
            )

            // Erase the specialist
            database.specialistDao().deleteSpecialist(specialist)

            // Should not exist
            Assert.assertNull(
                database.specialistDao().specialistByUsername(specialist.username)
            )
        }
    }

    @Test
    fun specialistCreateAndUpdate() {
        // Create mock specialist
        var specialist = MockGenerator.mockSpecialist().copy(name = "A")

        runBlocking(Dispatchers.IO) {
            // Insert the specialist
            database.specialistDao().upsertSpecialist(specialist)

            // Search specialist in database
            Assert.assertEquals(
                specialist,
                database.specialistDao().specialistByUsername(specialist.username)
            )

            // Erase the specialist
            specialist = specialist.copy(name = "B")
            database.specialistDao().upsertSpecialist(specialist)

            // Should not exist
            Assert.assertEquals(
                specialist,
                database.specialistDao().specialistByUsername(specialist.username)
            )
        }
    }

    @Test
    fun getAllSpecialists() {

        // Generate all specialists
        val specialists = List(10) { MockGenerator.mockSpecialist() }

        runBlocking(Dispatchers.IO) {

            // Insert all specialists
            specialists.forEach {
                database.specialistDao().upsertSpecialist(it)
            }

            // Check specialists
            Assert.assertEquals(specialists, database.specialistDao().allSpecialists())
        }
    }

    @Test
    fun specialistByCredentialsShouldReturnSpecialistOrNull() {
        // Generate specialist
        val specialists = List(10) { MockGenerator.mockSpecialist() }
        // Grab one
        val selected = specialists.random()

        runBlocking(Dispatchers.IO) {

            // Insert all specialists
            specialists.forEach {
                database.specialistDao().upsertSpecialist(it)
            }

            // Check specialists
            Assert.assertEquals(
                selected,
                database.specialistDao()
                    .specialistByCredentials(selected.username, selected.password!!)
            )

            // Check non existent specialist
            Assert.assertNull(
                database.specialistDao().specialistByCredentials(
                    selected.username,
                    Password("NON_VALID_PASSWORD")
                )
            )
        }
    }

}