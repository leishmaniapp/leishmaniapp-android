package com.leishmaniapp.usecases

import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.Username

/**
 * Authenticate [Specialist] into application
 */
fun interface IAuthenticationProvider {
    /**
     * Authenticate with credentials, returns [Specialist] or null
     */
    suspend fun authenticateSpecialist(username: Username, password: Password): Specialist?
}