package com.leishmaniapp.infrastructure.mock

import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.Username
import com.leishmaniapp.entities.disease.MockDisease
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.usecases.IAuthenticationProvider
import com.leishmaniapp.utils.MockGenerator
import javax.inject.Inject

/**
 * Authenticate using "admin" as both [Username] and [Password]
 */
class MockAuthenticationProvider @Inject constructor(
    private val applicationDatabase: ApplicationDatabase
) : IAuthenticationProvider {
    override suspend fun authenticateSpecialist(
        username: Username,
        password: Password
    ): Specialist? {
        return if (username.value == "admin" && password.value == "admin") {
            val mockSpecialist = MockGenerator.mockSpecialist().copy(diseases = setOf(MockDisease))
            applicationDatabase.specialistDao().upsertSpecialist(mockSpecialist)
            mockSpecialist
        } else {
            null
        }
    }
}