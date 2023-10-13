package com.leishmaniapp.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.leishmaniapp.background.infrastructure.DemoApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File
import java.lang.Exception
import java.net.UnknownHostException

@HiltWorker
class CustomWorker @AssistedInject constructor (
    @Assisted private val api : DemoApi,
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters){
    override suspend fun doWork(): Result {
        return try {
            Log.i("CustomWorker", "ingresando en el servcio de AWS")

            // Ruta de la imagen
            val localImagePath = "path/image.jpg"
            val fileToUpload = File(localImagePath)

            // Configura las credenciales de AWS (HAY QUE CONFIGURARLAS PREVIAMENTE)
            val transferUtility = TransferUtility.builder()
                .context(applicationContext)
                .awsConfiguration(AWSMobileClient.getInstance().configuration)
                .build()

            // Sube la imagen a S3
            val transferObserver = transferUtility.upload(
                "nombre--bucket", // Reemplazar
                "nombre-en-s3/imagen.jpg", // Reemplazar
                fileToUpload
            )

            // Define un listener para rastrear el progreso de la carga
            transferObserver.setTransferListener(object : TransferListener {
                override fun onStateChanged(id: Int, state: TransferState) {
                    if (state == TransferState.COMPLETED) {
                        Log.d("CustomWorker", "Subida exitosa")
                        Result.success()
                    } else {
                        Log.d("CustomWorker", "Error en la subida")
                        Result.failure()
                    }
                }

                override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                    // aca se puede rastrear el progreso de carga
                }

                override fun onError(id: Int, ex: Exception) {
                    Log.e("CustomWorker", "Error en la subida: $ex")
                    Result.failure()
                }
            })
            Result.success()
        }
        catch (e: Exception){
            Log.e("CustomWorker", "Error: $e")
            Result.retry()
        }
    }

}