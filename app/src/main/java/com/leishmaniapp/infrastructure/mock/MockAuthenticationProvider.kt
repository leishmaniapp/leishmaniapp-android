package com.leishmaniapp.infrastructure.mock

import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.Username
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.usecases.IAuthenticationProvider

class MockAuthenticationProvider : IAuthenticationProvider {
    override suspend fun authenticateSpecialist(
        username: Username,
        password: Password
    ): Specialist? {
        return if (username.value == "admin" && password.value == "admin") {
            MockGenerator.mockSpecialist();
        } else {
            null
        }
    }
}