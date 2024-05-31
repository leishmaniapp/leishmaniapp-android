package com.leishmaniapp.usecases

import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.Email

/**
 * Authenticate [Specialist] into application
 */
fun interface IAuthenticationProvider {
    /**
     * Authenticate with credentials, returns [Specialist] or null
     * TODO: Use typed exceptions
     */
    suspend fun authenticateSpecialist(email: Email, password: Password): Specialist?
}