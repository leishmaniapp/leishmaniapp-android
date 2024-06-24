package com.leishmaniapp.infrastructure.http

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import kotlin.jvm.Throws

/**
 * Handle [Exception] during HTTP requests
 */
class ExceptionInterceptor : Interceptor {

    /**
     * Catch and rethrow exceptions
     */
    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            return chain.proceed(chain.request())
        } catch (e: Exception) {
            throw e
        }
    }
}