package com.leishmaniapp.usecases

import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.Username

interface IAuthenticationProvider {
    suspend fun authenticateSpecialist(username: Username, password: Password): Specialist?
}