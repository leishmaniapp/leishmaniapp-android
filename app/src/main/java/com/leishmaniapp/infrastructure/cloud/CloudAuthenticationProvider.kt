package com.leishmaniapp.infrastructure.cloud

import android.util.Log
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthException.NotAuthorizedException
import com.amplifyframework.auth.AuthException.UserNotFoundException
import com.amplifyframework.auth.AuthSession
import com.amplifyframework.core.Amplify
import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.Username
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.usecases.IAuthenticationProvider
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CloudAuthenticationProvider @Inject constructor(
    val applicationDatabase: ApplicationDatabase
) : IAuthenticationProvider {
    override suspend fun authenticateSpecialist(
        username: Username, password: Password
    ): Specialist? {

        try {
            // Fetch current session
            val session = suspendCoroutine { continuation ->
                Amplify.Auth.fetchAuthSession({ session ->
                    continuation.resume(Either.Left(session))
                }, { exception ->
                    continuation.resume(Either.Right(exception))
                })
            }.fold({ it }, { throw it })

            if (session.isSignedIn) {
                // Finish current session
                suspendCoroutine { continuation ->
                    Amplify.Auth.signOut({
                        continuation.resume(Either.Left(Unit))
                    }, { exception ->
                        continuation.resume(Either.Right(exception))
                    })
                }.fold({}, { throw it })
            }

            // Sign in
            val authResult = suspendCoroutine { continuation ->
                Amplify.Auth.signIn(username.value, password.value, { result ->
                    continuation.resume(Either.Left(result))
                }, { exception ->
                    continuation.resume(Either.Right(exception))
                })
            }.fold({ it }, { throw it })

            if (!authResult.isSignInComplete) {
                return null
            }

            // Get current session
            val nameAttribute = suspendCoroutine { continuation ->
                Amplify.Auth.fetchUserAttributes(
                    { results -> continuation.resume(results) },
                    { err -> throw err })
            }.first { attribute ->
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

        } catch (ex: Throwable) {
            when (ex) {
                is UserNotFoundException, is NotAuthorizedException -> {
                    Log.e(
                        "CloudAuthenticationProvider",
                        "User not found, credentials don't match",
                        ex
                    )
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
        return null
    }
}