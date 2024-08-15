package com.leishmaniapp.infrastructure.service.lam

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import androidx.core.content.FileProvider
import com.leishmaniapp.analysis.core.AnalysisStatus
import com.leishmaniapp.analysis.lam.LamAnalysisRequest
import com.leishmaniapp.analysis.lam.LamAnalysisResponse
import com.leishmaniapp.analysis.lam.fromBundle
import com.leishmaniapp.analysis.lam.toBundle
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.DiagnosticElementName
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.entities.ModelDiagnosticElement
import com.leishmaniapp.domain.repository.ISamplesRepository
import com.leishmaniapp.domain.services.ILocalAnalysisService
import com.leishmaniapp.domain.types.Coordinates
import com.leishmaniapp.infrastructure.di.InjectScopeWithDefaultDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

/**
 * Connect and configure LAM modules inside the device
 */
class LamLocalAnalysisServiceImpl @Inject constructor(

    @ApplicationContext
    private val applicationContext: Context,

    /**
     * Process requests in the Default coroutine context
     */
    @InjectScopeWithDefaultDispatcher
    private val coroutineScope: CoroutineScope,

    /**
     * Sample repository for changing state and results
     */
    private val samplesRepository: ISamplesRepository,

    ) : ILocalAnalysisService {

    companion object {
        /**
         * TAG for using with [Log]
         */
        val TAG: String = LamLocalAnalysisServiceImpl::class.simpleName!!

        /**
         * [Intent] action to grab the service
         */
        private const val LAM_SERVICE_ACTION = "com.leishmaniapp.lam.ACTION_ANALYZE"

        /**
         * Base name for the LAM modules installed in device
         */
        private const val BASE_LAM_PACKAGE = "com.leishmaniapp.lam."

        /**
         * FileProvider authority
         */
        private const val FILEPROVIDER_AUTHORITY = "com.leishmaniapp.fileprovider"
    }

    /**
     * Set a new [stage] for a sample being analyzed
     */
    private suspend fun setSampleStage(diagnosis: UUID, sample: Int, stage: AnalysisStage) {
        withContext(Dispatchers.IO) {
            samplesRepository.setSampleStage(diagnosis, sample, stage = stage)
        }
    }

    /**
     * Set the LAM analysis results for a sample
     */
    private suspend fun setSampleResult(
        diagnosis: UUID,
        sample: Int,
        results: Set<ModelDiagnosticElement>
    ) {
        withContext(Dispatchers.IO) {
            // Get the sample from storage
            samplesRepository.getSampleForDiagnosis(diagnosis, sample).first()
                // Add the model results
                ?.withModelElements(results)?.let {
                    // Insert in storage with new stage
                    samplesRepository.upsertSample(it.copy(stage = AnalysisStage.Analyzed))
                }
        }
    }

    /**
     * Handler for new service connections/disconnection
     */
    private inner class CustomServiceConnectionHandler(val disease: Disease) : ServiceConnection {

        /**
         * Handle incoming responses
         */
        private val replyHandler = Handler(applicationContext.mainLooper) { msg ->

            LamAnalysisResponse.fromBundle(msg.data, applicationContext.classLoader)?.let { r ->
                Log.d(TAG, "LAM response for disease (${Disease}) = $r")

                // Set the analysis stage and results
                coroutineScope.launch {
                    when (r.results.status) {

                        AnalysisStatus.OK -> {
                            try {
                                // Get the LAM results
                                val results = r.results.results!!.map { (k, v) ->
                                    ModelDiagnosticElement(
                                        id = DiagnosticElementName.diagnosticElementNameById(k)!!,
                                        coordinates = v.map { Coordinates(x = it.x, y = it.y) }
                                            .toSet()
                                    )
                                }.toSet()

                                // Append them to image sample
                                setSampleResult(r.diagnosis, r.sample, results)

                            } catch (e: Exception) {
                                Log.e(TAG, "Invalid results from LAM", e)
                                setSampleStage(
                                    r.diagnosis,
                                    r.sample,
                                    AnalysisStage.ResultError
                                )
                            }
                        }

                        AnalysisStatus.INVALID_FILE -> setSampleStage(
                            r.diagnosis,
                            r.sample,
                            AnalysisStage.DeliverError
                        )

                        AnalysisStatus.UNPROCESSABLE_CONTENT -> setSampleStage(
                            r.diagnosis,
                            r.sample,
                            AnalysisStage.ResultError
                        )
                    }
                }

            } ?: Log.w(
                TAG,
                "Response for disease (${disease}) did not contained a valid LAM response"
            )

            // Handler expects more messages
            true
        }

        /**
         * LAM package name, grab it from [ComponentName.getPackageName]
         */
        private lateinit var lamPackage: String

        /**
         * [Messenger] instance for sending request to the module
         */
        private lateinit var outgoingMessenger: Messenger

        /**
         * [Messenger] instance for recieving analysis responses
         */
        private val incomingMessenger: Messenger = Messenger(replyHandler)

        /**
         * [Channel] for sending async requests
         */
        private val requestChannel = Channel<LamAnalysisRequest>()

        /**
         * Callback for service connection
         */
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            name?.let {

                // Set the variables
                lamPackage = it.packageName
                outgoingMessenger = Messenger(service)

                // Handle each request in a separate coroutine
                coroutineScope.launch {
                    requestChannel.receiveAsFlow().collect { request ->

                        // Send the request and set the analysis stage
                        try {
                            handleRequest(request)
                            setSampleStage(
                                request.diagnosis,
                                request.sample,
                                AnalysisStage.Analyzing
                            )
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to deliver request to LAM (${name.packageName})")
                            setSampleStage(
                                request.diagnosis,
                                request.sample,
                                AnalysisStage.DeliverError
                            )
                        }
                    }
                }

                Log.d(TAG, "Bound to a new service (${it.className})")
            } ?: Log.w(TAG, "Failed to bound service due to null Component name")
        }

        /**
         * Callback for service disconnection
         */
        override fun onServiceDisconnected(name: ComponentName?) {
            name?.let {
                coroutineScope.launch { disconnect(disease, unbind = false) }
                Log.d(TAG, "Service (${it.className}) was disconnected")
            } ?: Log.w(TAG, "A service requested disconnection but ComponentName was null")
        }

        /**
         * Send a [LamAnalysisRequest] to the currently connected LAM
         */
        fun handleRequest(request: LamAnalysisRequest) {
            // Grant permission to the package to read the image
            applicationContext.grantUriPermission(
                lamPackage,
                request.uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            // Create the message to be sent
            val message = Message().apply {
                data = request.toBundle()
                replyTo = incomingMessenger
            }

            // Send the message to the service
            outgoingMessenger.send(message)
        }

        /**
         * Enqueue a [LamAnalysisRequest] to the currently connected LAM and process when available
         */
        suspend fun sendRequest(request: LamAnalysisRequest) {
            requestChannel.send(request)
        }
    }

    /**
     * Map with service connections mapped to diseases
     */
    private val activeConnections: MutableMap<Disease, CustomServiceConnectionHandler> =
        mutableMapOf()

    /**
     * Connect/bind to a LAM module service and store it in [activeConnections],
     * requires the [applicationContext] in order to bind services
     */
    private suspend fun connect(disease: Disease): Boolean = withContext(Dispatchers.Default) {

        // LAM already connected, return true
        if (activeConnections.containsKey(disease)) {
            return@withContext true
        }

        // Create an intent with the LAM bound action
        val intent = Intent(LAM_SERVICE_ACTION).apply {
            // Set the LAM package to the disease one
            setPackage(BASE_LAM_PACKAGE + disease.id)
            // Grant permissions to read the Uri
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        // Create the service handler
        val handler = CustomServiceConnectionHandler(disease = disease)

        // Return the result of bind service
        return@withContext applicationContext.bindService(
            intent, handler, Context.BIND_AUTO_CREATE
        ).also { boundSuccessfully ->
            // Store the connection if bound was successfull
            Log.d(TAG, "LAM service bind for disease ($disease) result ($boundSuccessfully)")
            if (boundSuccessfully) {
                // Set the current active connection
                activeConnections[disease] = handler
            }
        }
    }

    /**
     * Disconnect/unbind from a LAM module service and delete it from [activeConnections],
     * the service does not get unbinded if [unbind] is set to false
     */
    private suspend fun disconnect(disease: Disease, unbind: Boolean = true): Boolean =
        withContext(Dispatchers.Default) {

            // Check if the service is stored
            val isConnected = activeConnections.containsKey(disease)
            if (isConnected) {
                // Unbind the service and delete it
                if (unbind) {
                    applicationContext.unbindService(activeConnections[disease]!!)
                }
                activeConnections.remove(disease)
                Log.d(TAG, "LAM service unbind for ($disease) successfull")
            }

            // If the service was not connected, return true
            isConnected
        }

    /**
     * Try to analyze a sample, return true if analysis is possible, false if LAM module is not installed
     * or throws an exception if some other problem occurss
     */
    override suspend fun tryAnalyze(sample: ImageSample): Result<Boolean> = try {

        // Check if disease is currently connected or try to connect
        if (!activeConnections.containsKey(sample.metadata.disease) &&
            !connect(sample.metadata.disease)
        ) {
            // Failed to connect, LAM does not exist
            Result.success(false)
        }

        // Create a copy of the image
        val cacheFile = withContext(Dispatchers.IO) {
            File.createTempFile(
                "lam_${sample.metadata.disease.id}_${sample.metadata.sample}_${sample.metadata.diagnosis}",
                sample.file!!.path.let { p -> p!!.substring(p.lastIndexOf(".")) })
        }

        // Delete the file when its no longer needed
        cacheFile.deleteOnExit()

        // Copy the image back
        withContext(Dispatchers.IO) {
            applicationContext.contentResolver.openInputStream(sample.file!!)?.use { ins ->
                FileOutputStream(cacheFile).use { outs ->
                    ins.copyTo(outs)
                }
            }
        }

        // Get the URI for the file
        val uri = FileProvider.getUriForFile(
            applicationContext,
            FILEPROVIDER_AUTHORITY,
            cacheFile
        )

        // Send the request to LAM module
        activeConnections[sample.metadata.disease]!!.sendRequest(
            LamAnalysisRequest(
                diagnosis = sample.metadata.diagnosis,
                sample = sample.metadata.sample,
                uri = uri
            )
        )

        // LAM module binded
        Result.success(true)

    } catch (e: Exception) {
        Result.failure(e)
    }
}