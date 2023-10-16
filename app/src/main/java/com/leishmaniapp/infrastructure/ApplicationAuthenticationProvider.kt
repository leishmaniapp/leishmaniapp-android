package com.leishmaniapp.infrastructure

import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.Username
import com.leishmaniapp.usecases.IAuthenticationProvider

class ApplicationAuthenticationProvider: IAuthenticationProvider {
    override suspend fun authenticateSpecialist(
        username: Username,
        password: Password
    ): Specialist? {
        TODO("Find specialist in database!")
    }
}