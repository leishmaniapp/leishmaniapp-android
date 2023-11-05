package com.leishmaniapp.infrastructure.cloud


import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import arrow.core.Either
import com.amplifyframework.api.rest.RestOptions
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.result.StorageUploadResult
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageProcessingPayload
import com.leishmaniapp.entities.ImageProcessingResponse
import com.leishmaniapp.entities.ImageQueryResponse
import com.leishmaniapp.persistance.entities.ImageRoom
import com.leishmaniapp.persistance.entities.ImageRoom.Companion.asRoomEntity
import com.leishmaniapp.usecases.IProcessingRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CloudProcessingRequest @Inject constructor(
    @ApplicationContext val context: Context
) : IProcessingRequest {

    private suspend fun uploadPhoto(image: ImageRoom): StorageUploadResult {
        // Get the im
        val file = File(image.path!!.path!!)
        val filePath = "${image.diagnosisUUID}/${file.name}"

        return suspendCoroutine { continuation ->
            Amplify.Storage.uploadInputStream(filePath,
                file.inputStream(),
                { result -> continuation.resume(result) },
                { error -> throw error })
        }
    }

    override suspend fun uploadImageToBucket(
        diagnosisId: UUID, image: Image
    ): Pair<String, String> {
        val key = uploadPhoto(image.asRoomEntity(diagnosisId)).key
        val bucketName = "diagnostic-images-repository124316-dev"
        return bucketName to "public/$key"
    }


    override suspend fun generatePayloadRequest(payload: ImageProcessingPayload) {
        val encodedRequest = Json.encodeToString(payload)
        val parameters = RestOptions.builder().addPath("/diagnosis/analyze")
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

    override suspend fun checkIfInternetConnectionIsAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetwork
        return networkInfo != null
    }

    override suspend fun checkImagesProcessedForDiagnosis(diagnosisId: UUID): List<ImageProcessingResponse> {
        val options = RestOptions.builder().addPath("/diagnosis/analyze")
            .addQueryParameters(mapOf("diagnosis" to diagnosisId.toString())).build()

        return suspendCoroutine { continuation ->
            Amplify.API.get(options, { response ->
                continuation.resume(Either.Left(response.data.asString()))
            }, { exception ->
                continuation.resume(Either.Right(exception))
            })
        }.fold({ content ->
            Json.decodeFromString(content)
        }, { exception ->
            throw exception
        })
    }

    override suspend fun checkImageResults(
        diagnosisId: UUID,
        imageSample: Int
    ): ImageQueryResponse {
        val options = RestOptions.builder().addPath("/diagnosis/query")
            .addQueryParameters(
                mapOf(
                    "diagnosis" to diagnosisId.toString(),
                    "sample" to imageSample.toString()
                )
            ).build()

        return suspendCoroutine { continuation ->
            Amplify.API.get(options, { response ->
                continuation.resume(Either.Left(response.data.asString()))
            }, { exception ->
                continuation.resume(Either.Right(exception))
            })
        }.fold({ content ->
            Json.decodeFromString(content)
        }, { exception ->
            throw exception
        })
    }
}