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

@HiltWorker
class DiagnosisResultsWorker @AssistedInject constructor(
    @ApplicationContext context: Context,
    @Assisted params: WorkerParameters,
    @Assisted val cloudProcessingRequest: IProcessingRequest,
    @Assisted val applicationDatabase: ApplicationDatabase,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {

        Log.d(DiagnosisResultsWorker::class.simpleName, "Background processing of images results")

        // Get all diagnoses
        val diagnoses = applicationDatabase.diagnosisDao().diagnosesNotFinishedAnalyzed()

        // For diagnosis in the application
        for (diagnosis in diagnoses) {

            Log.d(
                DiagnosisResultsWorker::class.simpleName,
                "Querying results for diagnosis: ${diagnosis.id}"
            )

            // Grab the images that require update on analysis status
            val images = applicationDatabase.imageDao()
                .imagesForDiagnosisWithStatus(diagnosis.id, ImageAnalysisStatus.Analyzing)

            // Check if images are empty
            if (images.isEmpty()) continue

            // Get cloud results
            val diagnosisResults = try {
                cloudProcessingRequest.checkImagesProcessedForDiagnosis(diagnosis.id)
            } catch (e: Exception) {
                Log.e(
                    DiagnosisResultsWorker::class.simpleName, "Failed to get diagnosis results", e
                )
                return Result.failure()
            }

            Log.d(
                DiagnosisResultsWorker::class.simpleName,
                "Got results ${diagnosisResults.size} from cloud"
            )

            // For each image result
            for (imageResult in diagnosisResults) {
                // Search the corresponding image
                val image = images.firstOrNull { it.sample == imageResult.sample }

                // Check if image exists
                if (image == null) {
                    Log.e(
                        this::class.simpleName!!,
                        "Failed to fetch image ${imageResult.sample} for diagnosis ${
                            diagnosis.id
                        }"
                    )
                    continue
                }

                // Update the image values
                val specialistElements =
                    image.elements.filterIsInstance<SpecialistDiagnosticElement>()

                val disease = diagnosis.disease
                val modelDiagnosticElements =
                    imageResult.analysis.map { (diagnosisModel, coordinates) ->
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

                // Store new image in database
                Log.i(
                    DiagnosisResultsWorker::class.simpleName,
                    "Updated sample ${updatedImage.sample} in database for diagnosis ${diagnosis.id}"
                )

                applicationDatabase.imageDao().upsertImage(updatedImage)
            }
        }

        return Result.retry()
    }
}