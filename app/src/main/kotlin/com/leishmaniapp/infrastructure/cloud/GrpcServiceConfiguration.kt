package com.leishmaniapp.infrastructure.cloud

import android.util.Log
import com.leishmaniapp.BuildConfig
import com.leishmaniapp.infrastructure.http.ExceptionInterceptor
import com.squareup.wire.GrpcClient
import kotlinx.coroutines.Dispatchers
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import java.time.Duration
import javax.inject.Inject

/**
 * Provide a [GrpcClient] for use within gRPC services
 */
data class GrpcServiceConfiguration(

    /**
     * HTTPS server connection string
     */
    private val endpoint: String = BuildConfig.REMOTE_ENDPOINT,

    /**
     * Server call timeout
     */
    private val timeoutSec: Long = BuildConfig.REMOTE_TIMEOUT.toLong(),
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
        // Interceptors
        .addInterceptor(ExceptionInterceptor())
        .addInterceptor(HttpLoggingInterceptor())
        // Disable TLS encryption
        .connectionSpecs(listOf(ConnectionSpec.CLEARTEXT))
        // Set the timeouts for remote calls
        .readTimeout(Duration.ofSeconds(timeoutSec))
        .writeTimeout(Duration.ofSeconds(timeoutSec))
        .callTimeout(Duration.ofSeconds(timeoutSec))
        // No HTTP2 upgrade roundtrip
        .protocols(listOf(Protocol.H2_PRIOR_KNOWLEDGE))
        .build()

    /**
     * Exposed gRPC client for calls
     */
    val client: GrpcClient = GrpcClient.Builder()
        .client(transport)
        .baseUrl(endpoint)
        .build()

    init {
        Log.d(TAG, "FilterChain: ${transport.interceptors.map { it::class }}")
    }
}
