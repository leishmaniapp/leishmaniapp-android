package com.leishmaniapp.infrastructure.http

import com.leishmaniapp.domain.services.IAuthorizationService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Add the 'authorization' header to HTTP requests
 */
class AuthorizationInterceptor @Inject constructor(

    /**
     * Get the authentication token
     */
    private val authorizationService: IAuthorizationService,

    ) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        // Get the authorization token
        runBlocking { authorizationService.credentials.first() }.let { credentials ->
            chain.proceed(
                // If token is present add the authorization header
                if (credentials != null) {
                    chain.request().newBuilder()
                        .header("Authorization", "Bearer ${credentials.token}")
                        .header("From", "Bearer ${credentials.email}")
                        .build()
                }
                // If not, just forward the request
                else chain.request()
            )
        }
}