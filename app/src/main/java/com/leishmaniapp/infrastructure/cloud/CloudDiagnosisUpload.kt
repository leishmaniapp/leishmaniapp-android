package com.leishmaniapp.infrastructure.cloud

import android.util.Log
import arrow.core.Either
import com.amplifyframework.api.rest.RestOptions
import com.amplifyframework.core.Amplify
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.usecases.IDiagnosisUpload
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CloudDiagnosisUpload @Inject constructor() : IDiagnosisUpload {
    override suspend fun uploadDiagnosis(diagnosis: Diagnosis) {

        val encodedRequest = Json.encodeToString(diagnosis)
        val parameters = RestOptions.builder().addPath("/diagnosis")
            .addBody(encodedRequest.toByteArray()).build()

        suspendCoroutine { continuation ->
            Amplify.API.post(parameters, {
                if (!it.code.isSuccessful) {
                    Log.e(
                        "PayloadRequest",
                        "POST failed (${it.code}) for request: $encodedRequest, got body: ${it.data.asString()}"
                    )
                    continuation.resume(Either.Right(it))
                } else {
                    Log.i(
                        "PayloadRequest",
                        "POST succeeded for request: $encodedRequest with body: ${it.data.asString()}"
                    )
                    continuation.resume(Either.Left(it))
                }
            }, {
                Log.e("PayloadRequest", "POST failed", it)
                continuation.resume(Either.Right(it))
            })
        }.isRight { exception ->
            if (exception is Throwable) throw exception
            else throw Exception("APIError")
        }
    }
}