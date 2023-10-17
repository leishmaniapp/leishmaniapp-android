package com.leishmaniapp.infrastructure.mock

import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.Username
import com.leishmaniapp.utils.MockGenerator
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.usecases.IAuthenticationProvider
import javax.inject.Inject

class MockAuthenticationProvider @Inject constructor(
    val applicationDatabase: ApplicationDatabase
) : IAuthenticationProvider {
    override suspend fun authenticateSpecialist(
        username: Username,
        password: Password
    ): Specialist? {
        return if (username.value == "admin" && password.value == "admin") {
            val mockSpecialist = MockGenerator.mockSpecialist()
            applicationDatabase.specialistDao().upsertSpecialist(mockSpecialist)
            mockSpecialist
        } else {
            null
        }
    }
}