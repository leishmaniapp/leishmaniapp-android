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
    private val tokenRepository: IAuthorizationService,

    ) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        // Get the authorization token
        runBlocking { tokenRepository.accessToken.first() }.let { token ->
            chain.proceed(
                // If token is present add the authorization header
                if (token != null) {
                    chain.request().newBuilder()
                        .header("authorization", "Bearer $token")
                        .build()
                }
                // If not, just forward the request
                else chain.request()
            )
        }
}