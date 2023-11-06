package com.leishmaniapp.infrastructure.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.leishmaniapp.entities.ImageAnalysisStatus
import com.leishmaniapp.entities.ModelDiagnosticElement
import com.leishmaniapp.entities.SpecialistDiagnosticElement
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.usecases.IProcessingRequest
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID

@HiltWorker
class ImageResultsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted val params: WorkerParameters,
    val cloudProcessingRequest: IProcessingRequest,
    val applicationDatabase: ApplicationDatabase,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {

        // Get parameters
        val diagnosisIdString = params.inputData.getString("diagnosis")
        val sample = params.inputData.getInt("sample", -1)

        if (diagnosisIdString == null || sample == -1) {
            Log.e(this::class.simpleName, "Invalid parameters for request")
            return Result.failure()
        }

        Log.d(
            DiagnosisResultsWorker::class.simpleName,
            "Requesting image results for $sample@$diagnosisIdString"
        )

        // Get variables
        val diagnosisUUID = UUID.fromString(diagnosisIdString)
        val image = applicationDatabase.imageDao().imageForDiagnosis(diagnosisUUID, sample)
        val diagnosis = applicationDatabase.diagnosisDao().diagnosisForId(diagnosisUUID)
        if (diagnosis == null || image == null) {
            Log.e(
                DiagnosisResultsWorker::class.simpleName, "Diagnosis or Image are not valid"
            )
            return Result.failure()
        }

        if (image.processed == ImageAnalysisStatus.Analyzed) {
            Log.d(this::class.simpleName!!, "Image was already been analyzed")
            return Result.success()
        }

        val analysisResults = try {
            cloudProcessingRequest.checkImageResults(diagnosisUUID, sample)
        } catch (e: Exception) {
            Log.e(
                DiagnosisResultsWorker::class.simpleName, "Failed to get image results", e
            )
            return Result.failure()
        }

        // If image hasn't been processed
        if (!analysisResults.processed)
            return Result.retry()


        // Update the image values
        val specialistElements =
            image.elements.filterIsInstance<SpecialistDiagnosticElement>()

        val disease = diagnosis.disease
        val modelDiagnosticElements =
            analysisResults.analysis.map { (diagnosisModel, coordinates) ->
                val elementName =
                    disease.elements.elementAt(disease.models.indexOf(diagnosisModel))

                ModelDiagnosticElement(elementName, diagnosisModel, coordinates.toSet())
            }

        // Update image
        val completeElements =
            setOf(specialistElements, modelDiagnosticElements).flatten().toSet()

        val updatedImage = image.copy(
            elements = completeElements,
            processed = ImageAnalysisStatus.Analyzed
        )

        // Update image in room
        applicationDatabase.imageDao().upsertImage(updatedImage)
        return Result.retry()
    }
}