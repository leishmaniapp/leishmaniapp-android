package com.leishmaniapp.infrastructure.cloud

import android.util.Log
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.cognito.exceptions.service.UserNotFoundException
import com.amplifyframework.auth.exceptions.NotAuthorizedException
import com.amplifyframework.kotlin.core.Amplify
import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.Username
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.usecases.IAuthenticationProvider
import javax.inject.Inject

class CloudAuthenticationProvider @Inject constructor(
    val applicationDatabase: ApplicationDatabase
) : IAuthenticationProvider {
    override suspend fun authenticateSpecialist(
        username: Username, password: Password
    ): Specialist? {
        try {
            // Fetch current session
            val session = Amplify.Auth.fetchAuthSession()
            if (session.isSignedIn) {
                // Finish current session
                Amplify.Auth.signOut()
            }

            // Sign in
            val authResult = Amplify.Auth.signIn(username.value, password.value)
            if (!authResult.isSignedIn) {
                return null;
            }

            // Get current session
            val nameAttribute = Amplify.Auth.fetchUserAttributes().first { attribute ->
                attribute.key.keyString == "name"
            }

            // Create the specialist
            val specialist = Specialist(
                username = username,
                password = password,
                name = nameAttribute.value
            )

            // Update the specialist in Database
            applicationDatabase.specialistDao().upsertSpecialist(specialist)
            return specialist

        } catch (ex: Exception) {
            when (ex) {
                is UserNotFoundException, is NotAuthorizedException -> {
                    Log.e(
                        "CloudAuthenticationProvider",
                        "User not found, credentials don't match",
                        ex
                    )

                    // Delete it from local database
                    applicationDatabase.specialistDao().deleteSpecialistWithUsername(username)
                }

                is AuthException -> {
                    Log.e("CloudAuthenticationProvider", "Failed to authenticate", ex)
                    ex.printStackTrace()

                    // Try authentication from database
                    return applicationDatabase.specialistDao()
                        .specialistByCredentials(username, password)
                }
            }

        }

        // Specialist not found
        return null;
    }
}