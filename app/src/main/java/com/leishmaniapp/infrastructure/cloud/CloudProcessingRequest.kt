package com.leishmaniapp.infrastructure.cloud


import android.content.Context
import android.util.Log
import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.asByteStream
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.storage.StorageException
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageProcessingPayload
import com.leishmaniapp.usecases.IProcessingRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class CloudProcessingRequest @Inject constructor(
    @ApplicationContext val context: Context
) : IProcessingRequest {

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    override suspend fun uploadImageToBucket(diagnosisId: UUID, image: Image): String {
        // Get the file
        val fileToUpload = File(image.path!!.path!!)

        // Check if the file exists
        if (!fileToUpload.exists() || !fileToUpload.canRead()) {
            Log.e("CloudProcessingRequest", "ImagePath does not exist")
            throw IOException(image.path.path!!)
        }

        // Picture key
        val pictureKey = String.format("%s/%s", diagnosisId, fileToUpload.name)
        Log.d("CloudProcessingRequest", "Uploading picture `$pictureKey` from file `$fileToUpload`")

        // Upload entity
        try {
            return withContext(Dispatchers.IO) {

                S3Client.fromEnvironment().use { s3 ->
                    val request = PutObjectRequest {
                        bucket = "diagnostic-images-repository22821-dev"
                        key = pictureKey
                        body = fileToUpload.asByteStream()
                    }

                    s3.putObject(request).eTag!!
                }

//                val upload =
//                    Amplify.Storage.uploadInputStream(
//                        pictureKey,
//                        fileToUpload.inputStream(),
//                        options = StorageUploadInputStreamOptions.builder()
//                            .accessLevel(StorageAccessLevel.PUBLIC)
//                            .contentType("text/plain")
//                            .build()
//                    )
//
//                return@withContext withTimeout(15000) {
//                    // Make the request
//                    val result = upload.result()
//                    Log.i("CloudProcessingRequest", "Uploaded image $result")
//
//                    // Return the key
//                    return@withTimeout result.key
//                }
            }
        } catch (e: StorageException) {
            Log.e("CloudProcessingRequest", "Failed to store image: $pictureKey", e)
            throw e;
        } catch (e: TimeoutCancellationException) {
            Log.e("CloudProcessingRequest", "Picture upload timed out", e)
            throw e;
        }
    }

    override suspend fun generatePayloadRequest(payload: ImageProcessingPayload) {
        TODO("Not yet implemented")
    }

}