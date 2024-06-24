package com.leishmaniapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.leishmaniapp.domain.disease.Disease
import javax.inject.Inject

/**
 * Handle diagnosis variables
 */
class DiagnosisViewModel @Inject constructor(

    /**
     * Keep state accross configuration changes
     */
    savedStateHandle: SavedStateHandle,

    ) : ViewModel() {

    companion object {
        /**
         * TAG to use within [Log]
         */
        val TAG: String = DiagnosisViewModel::class.simpleName!!
    }

    private val _disease: MutableLiveData<Disease?> =
        savedStateHandle.getLiveData(
            "disease", null
        )

    /**
     * Selected disease by the specialist
     */
    val disease: LiveData<Disease?> = _disease


    /**
     * Select a new disease for diagnosis
     */
    fun setDisease(disease: Disease) {
        Log.i(TAG, "Disease changed to (${disease.id})")
        _disease.value = disease
    }

    /**
     * Delete the already selected [Disease]
     */
    fun dismissDisease() {
        Log.i(TAG, "Disease deleted, using null")
        _disease.value = null
    }

    /**
     * Restart the state
     */
    fun dismissAll() {
        dismissDisease()
    }
}