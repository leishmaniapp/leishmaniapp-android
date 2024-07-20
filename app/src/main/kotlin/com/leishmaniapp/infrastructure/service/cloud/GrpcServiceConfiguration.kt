package com.leishmaniapp.infrastructure.service.cloud

import android.util.Log
import com.leishmaniapp.BuildConfig
import com.leishmaniapp.infrastructure.http.AuthorizationInterceptor
import com.leishmaniapp.infrastructure.http.ExceptionInterceptor
import com.squareup.wire.GrpcClient
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import java.time.Duration

/**
 * Provide a [GrpcClient] for use within gRPC services
 */
class GrpcServiceConfiguration(

    /**
     * HTTPS server connection string
     */
    endpoint: String = BuildConfig.REMOTE_ENDPOINT,

    /**
     * Server call timeout
     */
    val timeoutSec: Long = BuildConfig.REMOTE_TIMEOUT.toLong(),

    // Interceptors
    exceptionInterceptor: ExceptionInterceptor,
    authorizationInterceptor: AuthorizationInterceptor,
) {

    companion object {
        /**
         * TAG to use with [Log]
         */
        val TAG: String = GrpcServiceConfiguration::class.simpleName!!
    }

    /**
     * HTTP2 client with no TLS configuration as transport for gRPC
     */
    private val transport: OkHttpClient = OkHttpClient.Builder()
        // Timeout
        .readTimeout(Duration.ofDays(15))
        // Interceptors
        .addInterceptor(exceptionInterceptor)
        .addInterceptor(authorizationInterceptor)
        .addInterceptor(HttpLoggingInterceptor())
        // Disable TLS encryption
        .connectionSpecs(listOf(ConnectionSpec.CLEARTEXT))
        // No HTTP2 upgrade roundtrip
        .protocols(listOf(Protocol.H2_PRIOR_KNOWLEDGE)).build()

    /**
     * Exposed gRPC client for calls
     */
    val client: GrpcClient = GrpcClient.Builder().client(transport).baseUrl(endpoint).build()

    init {
        Log.d(TAG, "FilterChain: ${transport.interceptors.map { it::class }}")
    }
}
