package com.leishmaniapp.infrastructure.cloud


import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import arrow.core.Either
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.result.StorageUploadResult
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageProcessingPayload
import com.leishmaniapp.persistance.entities.ImageRoom
import com.leishmaniapp.persistance.entities.ImageRoom.Companion.asRoomEntity
import com.leishmaniapp.usecases.IProcessingRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CloudProcessingRequest @Inject constructor(
    @ApplicationContext val context: Context
) : IProcessingRequest {

    companion object {
        const val BASE_URL = "https://321xsmnj04.execute-api.us-east-1.amazonaws.com/debugging"
        const val UPLOAD_PATH = "/diagnosis/analyze"
    }

    private suspend fun uploadPhoto(image: ImageRoom): StorageUploadResult {
        // Get the im
        val file = File(image.path!!.path!!)

        val filePath = "${image.diagnosisUUID}/${file.name}"

        return suspendCoroutine { continuation ->
            Amplify.Storage.uploadInputStream(
                filePath,
                file.inputStream(),
                { result -> continuation.resume(result) },
                { error -> throw error }
            )
        }
    }

    override suspend fun uploadImageToBucket(diagnosisId: UUID, image: Image): String {
        val key = uploadPhoto(image.asRoomEntity(diagnosisId)).key
        val fullPath = suspendCoroutine { continuation ->
            Amplify.Storage.getUrl(key, { urlResult ->
                continuation.resume(Either.Left(urlResult.url))
            }, { ex ->
                continuation.resume(Either.Right(ex))
            })
        }.fold({ it.path }, { e -> throw e })
        val bucketName = "diagnostic-images-repository124316-dev"
        return "s3://$bucketName$fullPath"
    }

    private val client = OkHttpClient()

    override suspend fun generatePayloadRequest(payload: ImageProcessingPayload) {
        val body = Json.encodeToString(payload)
        val request = Request.Builder().url("$BASE_URL$UPLOAD_PATH").post(
            body.toRequestBody("application/json".toMediaType())
        ).build()

        suspendCoroutine { continuation ->
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resume(Either.Right(e))
                }
                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(Either.Left(response.code))
                }
            })
        }.isRight { exception ->
            throw exception
        }
    }

    override suspend fun checkIfInternetConnectionIsAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetwork
        return networkInfo != null
    }

}