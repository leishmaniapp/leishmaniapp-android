package com.leishmaniapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Email
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
class RoomSpecialistsDaoTests {

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
            database.specialistsRepository().upsertSpecialist(specialist)

            // Search specialist in database
            Assert.assertEquals(
                specialist,
                database.specialistsRepository().specialistByUsername(specialist.email)
            )

            // Erase the specialist
            database.specialistsRepository().deleteSpecialist(specialist)

            // Should not exist
            Assert.assertNull(
                database.specialistsRepository().specialistByUsername(specialist.email)
            )
        }
    }

    @Test
    fun specialistCreateSearchAndDeleteWithCredentials() {
        // Create mock specialist
        val specialist = MockGenerator.mockSpecialist()

        runBlocking(Dispatchers.IO) {
            // Insert the specialist
            database.specialistsRepository().upsertSpecialist(specialist)

            // Search specialist in database
            Assert.assertEquals(
                specialist,
                database.specialistsRepository().specialistByUsername(specialist.email)
            )

            // Erase the specialist
            database.specialistsRepository().deleteSpecialistByUsername(specialist.email)

            // Should not exist
            Assert.assertNull(
                database.specialistsRepository().specialistByUsername(specialist.email)
            )
        }
    }

    @Test
    fun deleteNonExistentSpecialistDoesNothing() {
        runBlocking {
            // Erase the specialist
            database.specialistsRepository().deleteSpecialistByUsername(Email("Does not exist!"))
        }
    }

    @Test
    fun specialistCreateAndUpdate() {
        // Create mock specialist
        var specialist = MockGenerator.mockSpecialist().copy(name = "A")

        runBlocking(Dispatchers.IO) {
            // Insert the specialist
            database.specialistsRepository().upsertSpecialist(specialist)

            // Search specialist in database
            Assert.assertEquals(
                specialist,
                database.specialistsRepository().specialistByUsername(specialist.email)
            )

            // Erase the specialist
            specialist = specialist.copy(name = "B")
            database.specialistsRepository().upsertSpecialist(specialist)

            // Should not exist
            Assert.assertEquals(
                specialist,
                database.specialistsRepository().specialistByUsername(specialist.email)
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
                database.specialistsRepository().upsertSpecialist(it)
            }

            // Check specialists
            Assert.assertEquals(specialists, database.specialistsRepository().allSpecialists())
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
                database.specialistsRepository().upsertSpecialist(it)
            }

            // Check specialists
            Assert.assertEquals(
                selected,
                database.specialistsRepository()
                    .specialistByCredentials(selected.email, selected.password!!)
            )

            // Check non existent specialist
            Assert.assertNull(
                database.specialistsRepository().specialistByCredentials(
                    selected.email,
                    Password("NON_VALID_PASSWORD")
                )
            )
        }
    }

}