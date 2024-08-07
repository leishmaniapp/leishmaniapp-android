package com.leishmaniapp.presentation.viewmodel.deprecated//package com.leishmaniapp.presentation.viewmodel
//
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.util.Log
//import androidx.core.content.FileProvider
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.work.WorkManager
//import com.leishmaniapp.entities.Image
//import com.leishmaniapp.entities.ImageAnalysisStage
//import com.leishmaniapp.entities.Patient
//import com.leishmaniapp.entities.Specialist
//import com.leishmaniapp.entities.SpecialistDiagnosticElement
//import com.leishmaniapp.entities.disease.Disease
//import com.leishmaniapp.infrastructure.persistance.ApplicationDatabase
//import com.leishmaniapp.domain.services.IDiagnosisSharing
//import com.leishmaniapp.domain.services.IPictureStandardization
//import com.leishmaniapp.domain.services.IProcessingRequest
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.runBlocking
//import kotlinx.coroutines.withContext
//import java.util.UUID
//import javax.inject.Inject
//
//@HiltViewModel
//class DiagnosisViewModel @Inject constructor(
//    val savedStateHandle: SavedStateHandle,
//    val applicationDatabase: ApplicationDatabase,
//    private val diagnosisShare: IDiagnosisSharing,
//    private val pictureStandardization: IPictureStandardization,
//    private val imageProcessingRequest: IProcessingRequest
//) : ViewModel() {
//
//    /**
//     * The currently selected diagnosis is being created or continued?
//     */
//    var isNewDiagnosis: Boolean = false
//        get() = savedStateHandle["isNewDiagnosis"] ?: false
//        set(value) {
//            savedStateHandle["isNewDiagnosis"] = value
//            field = value
//        }
//
//    /**
//     * Currently selected diagnosis
//     */
//    val currentDiagnosis = MutableStateFlow<com.leishmaniapp.domain.entities.Diagnosis?>(null)
//
//    fun setCurrentDiagnosis(diagnosis: com.leishmaniapp.domain.entities.Diagnosis?) {
//        currentDiagnosis.value = diagnosis
//        savedStateHandle["currentDiagnosis"] = currentDiagnosis.value
//    }
//
//    /**
//     * Currently selected image
//     */
//    val currentImage = MutableStateFlow<Image?>(null)
//
//    fun setCurrentImage(image: Image?) {
//        currentImage.value = image
//        savedStateHandle["currentImage"] = currentImage.value
//    }
//
//    /**
//     * Currently selected image as Flow from database
//     */
//    val imageFlow: Flow<ImageRoom?>?
//        get() = runBlocking {
//            if (currentDiagnosis.value != null && currentImage.value != null) applicationDatabase.imageSamplesRepository()
//                .imageForDiagnosisFlow(currentDiagnosis.value!!.id, currentImage.value!!.sample)
//            else null
//        }
//
//    val diagnosisFlow: Flow<DiagnosisRoom?>?
//        get() = runBlocking {
//            if (currentDiagnosis.value != null) applicationDatabase.diagnosesRepository()
//                .diagnosisForIdFlow(currentDiagnosis.value!!.id)
//            else null
//        }
//
//    /**
//     * List of images for current diagnosis as Flow from database
//     */
//    val imagesForDiagnosisFlow: Flow<List<ImageRoom>?>?
//        get() = runBlocking {
//            if (currentDiagnosis.value != null) applicationDatabase.imageSamplesRepository()
//                .allImagesForDiagnosisFlow(
//                    currentDiagnosis.value!!.id
//                )
//            else null
//        }
//
//    /**
//     * Current image analysis worker
//     */
//    val currentWorkerId = MutableStateFlow<UUID?>(null)
//
//    init {
//        // Read from bundle when activity is destroyed
//        savedStateHandle.get<com.leishmaniapp.domain.entities.Diagnosis?>("currentDiagnosis")?.let { savedDiagnosis ->
//            currentDiagnosis.value = savedDiagnosis
//        }
//        savedStateHandle.get<Image?>("currentImage")?.let { savedImage ->
//            currentImage.value = savedImage
//        }
//        savedStateHandle.get<UUID?>("currentWorkerId")?.let { workerId ->
//            currentWorkerId.value = workerId
//        }
//    }
//
//    /**
//     * Get all diagnoses for a given patient
//     */
//    fun diagnosesForPatient(patient: Patient): List<com.leishmaniapp.domain.entities.Diagnosis> {
//        val retrievalDiagnosis: MutableList<com.leishmaniapp.domain.entities.Diagnosis> = mutableListOf()
//
//        runBlocking {
//            val diagnosis = applicationDatabase.diagnosesRepository().diagnosesForPatient(patient)
//
//            for (element in diagnosis) {
//                val elementSpecialist = applicationDatabase.specialistsRepository()
//                    .specialistByUsername(element.specialistEmail)
//                val elementPatient = applicationDatabase.patientsRepository()
//                    .patientById(element.patientIdDocument, element.patientIdType)
//                val elementImages = applicationDatabase.imageSamplesRepository().allImagesForDiagnosis(element.id)
//
//                // Add diagnosis to return value
//                retrievalDiagnosis.add(
//                    element.asApplicationEntity(elementSpecialist!!,
//                        elementPatient!!,
//                        elementImages.map { it.asApplicationEntity() })
//                )
//            }
//
//        }
//
//        return retrievalDiagnosis.toList()
//    }
//
//    /**
//     * Share the current diagnosis as a PDF
//     */
//
//    fun shareCurrentDiagnosis(context: Context) {
//        val file = runBlocking {
//            diagnosisShare.generateDiagnosisFile(currentDiagnosis.value!!)
//        }
//
//        Log.d("DiagnosisShare", "Stored in file: ${file.absolutePath}")
//
//        val uri = FileProvider.getUriForFile(context, "com.leishmaniapp", file)
//        val intent = Intent(Intent.ACTION_SEND).apply {
//            type = "application/pdf"
//            putExtra(Intent.EXTRA_STREAM, uri)
//        }
//
//        context.startActivity(Intent.createChooser(intent, "Share via"))
//    }
//
//    fun startDiagnosisResultOneTimeWorker(context: Context) {
//        // TODO: Fix this
////        val workRequest = OneTimeWorkRequestBuilder<DiagnosisResultsWorker>().setConstraints(
////            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
////        ).build()
////
////        WorkManager.getInstance(context).enqueue(workRequest)
//    }
//
//    fun startDiagnosisUploadOneTimeWorker(context: Context, diagnosisId: UUID) {
//        // TODO: Fix this
////        val workRequest = OneTimeWorkRequestBuilder<DiagnosisUploadWorker>().setInputData(
////            Data.Builder().putString("diagnosis", diagnosisId.toString()).build()
////        ).setConstraints(
////            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
////        ).build()
////
////        WorkManager.getInstance(context).enqueue(workRequest)
//    }
//
//    fun startDiagnosisResultsBackgroundWorker(context: Context) {
//        // TODO: Fix this
////        // Call the worker
////        val workRequest =
////            PeriodicWorkRequestBuilder<DiagnosisResultsWorker>(Duration.ofSeconds(60)).setConstraints(
////                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
////            ).build()
////
////        // Enqueue the request
////        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
////            DiagnosisResultsWorker::class.simpleName!!,
////            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
////            workRequest
////        )
//    }
//
//    fun stopDiagnosisResultsBackgroundWorker(context: Context) {
//        // TODO: Fix this
////        // Cancel work manager
////        WorkManager.getInstance(context)
////            .cancelUniqueWork(DiagnosisResultsWorker::class.simpleName!!)
//    }
//
//    fun startImageResultsWorker(context: Context, diagnosis: UUID, sample: Int) {
//        // TODO: Fix this
////        // Call the worker
////        val workRequest =
////            PeriodicWorkRequestBuilder<ImageResultsWorker>(Duration.ofSeconds(5)).setInputData(
////                Data.Builder().putString("diagnosis", diagnosis.toString()).putInt("sample", sample)
////                    .build()
////            ).setInitialDelay(Duration.ofSeconds(10)).setConstraints(
////                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
////            ).build()
////
////        // Enqueue the request
////        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
////            ImageResultsWorker::class.simpleName!!,
////            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
////            workRequest
////        )
//    }
//
//    fun stopImageResultsWorker(context: Context) {
//        // TODO: Fix this
////        // Cancel work manager
////        WorkManager.getInstance(context).cancelUniqueWork(ImageResultsWorker::class.simpleName!!)
//    }
//
//    /**
//     * Create a new diagnosis
//     */
//    fun startNewDiagnosis(
//        context: Context, patient: Patient, specialist: Specialist, disease: Disease
//    ) {
//        // Create a new diagnosis
//        runBlocking {
//            updateDiagnosis(
//                com.leishmaniapp.domain.entities.Diagnosis(
//                    specialist,
//                    patient,
//                    disease
//                )
//            )
//        }
//        isNewDiagnosis = true
//
//        startDiagnosisResultsBackgroundWorker(context)
//    }
//
//    /**
//     * Crop and resize image in order to fit use cases
//     */
//    fun standardizeImage(uri: Uri): Int? {
//        if (pictureStandardization.cropPicture(uri)) {
//            return pictureStandardization.scalePicture(uri)
//        }
//        return null
//    }
//
//    /**
//     * Store the image in the database
//     */
//    fun storeImageInDatabase() {
//        runBlocking {
//            applicationDatabase.imageSamplesRepository()
//                .upsertImage(currentImage.value!!.asRoomEntity(currentDiagnosis.value!!.id))
//        }
//    }
//
//    /**
//     * Repeat the picture take
//     */
//    fun discardAndRepeatCurrentImage(context: Context) {
//        stopImageResultsWorker(context)
//        // Cancel current request
//        if (currentWorkerId.value != null) {
//            WorkManager.getInstance(context).cancelWorkById(currentWorkerId.value!!)
//        }
//
//        // Erase image from database
//        runBlocking {
//            applicationDatabase.imageSamplesRepository()
//                .deleteImage(currentImage.value!!.asRoomEntity(currentDiagnosis.value!!.id))
//        }
//
//        // Set current image to null
//        setCurrentImage(null)
//    }
//
//
//    fun updateImage(image: Image) {
//        runBlocking {
//            setCurrentImage(image)
//            applicationDatabase.imageSamplesRepository()
//                .upsertImage(image.asRoomEntity(currentDiagnosis.value!!.id))
//        }
//    }
//
//    suspend fun updateDiagnosis(diagnosis: com.leishmaniapp.domain.entities.Diagnosis) {
//        setCurrentDiagnosis(diagnosis)
//        applicationDatabase.diagnosesRepository().upsertDiagnosis(diagnosis.asRoomEntity())
//    }
//
//    suspend fun setImageAsDeferred() {
//        applicationDatabase.imageSamplesRepository().upsertImage(
//            currentImage.value!!.copy(processed = ImageAnalysisStage.Deferred)
//                .asRoomEntity(currentDiagnosis.value!!.id)
//        )
//    }
//
//    suspend fun analyzeImage(context: Context) {
//        // TODO: Fix this
////        // Obtain data to worker
////        val data = Data.Builder().putString("diagnosis", currentDiagnosis.value!!.id.toString())
////            .putInt("sample", currentImage.value!!.sample).build()
////
////        // Build the worker
////        val worker = OneTimeWorkRequestBuilder<ImageProcessingWorker>().setConstraints(
////            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
////        ).setInputData(data).setBackoffCriteria(
////            backoffPolicy = BackoffPolicy.LINEAR, duration = Duration.ofSeconds(15)
////        ).build()
////
////        // Prompt the worker
////        WorkManager.getInstance(context).enqueue(worker)
////
////        // Return the worker ID
////        currentWorkerId.value = worker.id
////        savedStateHandle["currentWorkerId"] = currentImage.value
////
////        // Image is deferred because no internet connection is available
////        if (!imageProcessingRequest.checkIfInternetConnectionIsAvailable()) {
////            setImageAsDeferred()
////        }
////
////        val workerInfo =
////            WorkManager.getInstance(context).getWorkInfoByIdLiveData(currentWorkerId.value!!)
////                .asFlow()
////
////        var workerWasCalled = false
////        var coroutineWasExecuted = false
////
////        try {
////            withTimeout(15_000) {
////                workerInfo.collect { info ->
////                    when (info.state) {
////                        // Resume execution
////                        WorkInfo.State.RUNNING, WorkInfo.State.FAILED -> coroutineWasExecuted = true
////
////                        WorkInfo.State.SUCCEEDED -> if (!workerWasCalled) {
////                            startImageResultsWorker(
////                                context, currentDiagnosis.value!!.id, currentImage.value!!.sample
////                            )
////                            workerWasCalled = true
////                        }
////
////                        // Continue waiting
////                        WorkInfo.State.BLOCKED, WorkInfo.State.CANCELLED, WorkInfo.State.ENQUEUED -> Unit
////                    }
////                }
////            }
////        } catch (e: TimeoutCancellationException) {
////            // Check if coroutine was resumed
////            if (!coroutineWasExecuted) setImageAsDeferred()
////        }
//    }
//
//    /**
//     * Check if specialist is able to continue to next image
//     */
//    fun canContinueDiagnosisNextImage(): Boolean {
//        // Make sure specialist has provided his results
//        val diseaseElements = currentDiagnosis.value!!.disease.elements.toSet()
//        val currentDiagnosisElements =
//            currentImage.value!!.elements.filterIsInstance<SpecialistDiagnosticElement>()
//                .map { it.id }.toSet()
//
//        return (diseaseElements == currentDiagnosisElements)
//    }
//
//    suspend fun continueDiagnosisNextImage(context: Context) {
//        stopImageResultsWorker(context)
//        withContext(Dispatchers.IO) {
//            if (!canContinueDiagnosisNextImage()) {
//                throw IllegalStateException("Specialist is missing result")
//            }
//
//            // Take last photo and update it
//            updateImage(
//                applicationDatabase.imageSamplesRepository()
//                    .imageForDiagnosis(currentDiagnosis.value!!.id, currentImage.value!!.sample)!!
//                    .asApplicationEntity()
//            )
//
//            // Update the current diagnosis
//            updateDiagnosis(currentDiagnosis.value!!.appendImage(currentImage.value!!))
//        }
//
//        // Set current image to null
//        setCurrentImage(null)
//    }
//
//    suspend fun finishDiagnosisPictureTaking(context: Context) {
//
//        if (currentDiagnosis.value!!.samples == 0) {
//            throw IllegalStateException("Cannot finish diagnosis with no images")
//        }
//
//        stopImageResultsWorker(context)
//
//        withContext(Dispatchers.IO) {
//            // Delete image if not processed
//            val image = applicationDatabase.imageSamplesRepository()
//                .imageForDiagnosis(currentDiagnosis.value!!.id, currentImage.value!!.sample)!!
//
//            if (image.processed == ImageAnalysisStage.NotAnalyzed) {
//                applicationDatabase.imageSamplesRepository().deleteImage(image)
//            } else {
//                // Take last photo and update it
//                updateImage(
//                    applicationDatabase.imageSamplesRepository().imageForDiagnosis(
//                        currentDiagnosis.value!!.id, currentImage.value!!.sample
//                    )!!.asApplicationEntity()
//                )
//            }
//
//            // Update the current diagnosis
//            updateDiagnosis(currentDiagnosis.value!!.appendImage(currentImage.value!!))
//        }
//
//        // Set current image to null
//        setCurrentImage(null)
//    }
//
//    suspend fun getAwaitingDiagnosis(specialist: Specialist): List<com.leishmaniapp.domain.entities.Diagnosis> {
//        val diagnosis = withContext(Dispatchers.IO) {
//            applicationDatabase.diagnosesRepository()
//                .diagnosesForSpecialistNotFinished(specialist.email)
//        }
//
//        return diagnosis.filter { !it.finalized }.map { diagnosisRoom ->
//            val patient = withContext(Dispatchers.IO) {
//                applicationDatabase.patientsRepository()
//                    .patientById(diagnosisRoom.patientIdDocument, diagnosisRoom.patientIdType)
//            }!!
//
//            val images = withContext(Dispatchers.IO) {
//                applicationDatabase.imageSamplesRepository().allImagesForDiagnosis(diagnosisRoom.id)
//                    .map { it.asApplicationEntity() }
//            }
//
//            diagnosisRoom.asApplicationEntity(specialist, patient, images)
//        }
//    }
//
//    fun sendDiagnosisToBackgroundProcessing(context: Context) {
//        stopImageResultsWorker(context)
//        stopDiagnosisResultsBackgroundWorker(context)
//
//        setCurrentDiagnosis(null)
//        setCurrentImage(null)
//        isNewDiagnosis = false
//    }
//
//    suspend fun finalizeDiagnosis(context: Context) {
//        stopImageResultsWorker(context)
//        stopDiagnosisResultsBackgroundWorker(context)
//        withContext(Dispatchers.IO) {
//            updateDiagnosis(currentDiagnosis.value!!.copy(finalized = true))
//            startDiagnosisUploadOneTimeWorker(context, currentDiagnosis.value!!.id)
//        }
//    }
//
//    suspend fun discardDiagnosis(context: Context) {
//        stopImageResultsWorker(context)
//        stopDiagnosisResultsBackgroundWorker(context)
//        withContext(Dispatchers.IO) {
//            applicationDatabase.imageSamplesRepository().allImagesForDiagnosis(currentDiagnosis.value!!.id)
//                .forEach { imageRoom ->
//                    applicationDatabase.imageSamplesRepository().deleteImage(imageRoom)
//                }
//            applicationDatabase.diagnosesRepository()
//                .deleteDiagnosis(currentDiagnosis.value!!.asRoomEntity())
//        }
//
//        setCurrentDiagnosis(null)
//        setCurrentImage(null)
//    }
//
//    fun restartState() {
//        isNewDiagnosis = false
//
//        setCurrentImage(null)
//        setCurrentDiagnosis(null)
//    }
//}