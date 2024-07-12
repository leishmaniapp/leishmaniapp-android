package com.leishmaniapp.infrastructure.http

import android.util.Log
import com.leishmaniapp.domain.entities.Credentials
import com.leishmaniapp.domain.services.IAuthorizationService
import com.leishmaniapp.infrastructure.di.InjectScopeWithIODispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
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
    authorizationService: IAuthorizationService,

    @InjectScopeWithIODispatcher
    coroutineScope: CoroutineScope

) : Interceptor {

    companion object {
        /**
         * TAG for [Log]
         */
        val TAG: String = AuthorizationInterceptor::class.simpleName!!
    }

    private val credentials: StateFlow<Credentials?> =
        authorizationService.credentials.stateIn(coroutineScope, SharingStarted.Eagerly, null)

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            // If token is present add the authorization header
            if (credentials.value != null) {
                Log.d(TAG, "Credentials for user (${credentials.value!!.email}) gathered")
                chain.request().newBuilder()
                    .header("Authorization", "Bearer ${credentials.value!!.token}")
                    .header("From", credentials.value!!.email)
                    .build()
            }
            // If not, just forward the request
            else {
                Log.w(TAG, "Credentials not found, requests will not provide Authorization header")
                chain.request()
            }
        )
}