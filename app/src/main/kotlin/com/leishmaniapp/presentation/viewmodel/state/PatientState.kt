package com.leishmaniapp.presentation.viewmodel.state

import android.os.Parcelable
import com.leishmaniapp.domain.exceptions.LeishmaniappException
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class PatientState : Parcelable {

    /**
     * Data hasn't been initialize
     */
    @Parcelize
    data object NotReady: PatientState()

    /**
     * No operation is being processed
     */
    @Parcelize
    data object None : PatientState()

    /**
     * Operation is being made
     */
    @Parcelize
    data object Busy : PatientState()

    /**
     * Latest operation resulted in error
     */
    @Parcelize
    data class Error(val err: LeishmaniappException) : PatientState()

    /**
     * Latest operation was successfull, ej. Database insertion
     */
    @Parcelize
    data object Success: PatientState()
}