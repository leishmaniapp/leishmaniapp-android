package com.leishmaniapp.infrastructure.http

import android.util.Log
import com.leishmaniapp.domain.services.IAuthorizationService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
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

    companion object {
        /**
         * TAG for [Log]
         */
        val TAG: String = AuthorizationInterceptor::class.simpleName!!
    }

    override fun intercept(chain: Interceptor.Chain): Response =
        // Get the authorization token
        runBlocking { authorizationService.credentials.first() }.let { credentials ->

            if (credentials != null) {
                Log.d(TAG, "Credentials for user (${credentials.email}) gathered")
            } else {
                Log.w(TAG, "Credentials not found, requests will not provide Authorization header")
            }

            chain.proceed(
                // If token is present add the authorization header
                if (credentials != null) {
                    chain.request().newBuilder()
                        .header("Authorization", "Bearer ${credentials.token}")
                        .header("From", credentials.email)
                        .build()
                }
                // If not, just forward the request
                else chain.request()
            )
        }
}