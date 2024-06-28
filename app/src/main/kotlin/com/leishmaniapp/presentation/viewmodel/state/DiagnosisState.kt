package com.leishmaniapp.presentation.viewmodel.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

/**
 * Current diagnosis flow of control status
 */
@Parcelize
sealed class DiagnosisState: Parcelable {

    /**
     * No diagnosis has been created, not diagnosticating
     */
    @Parcelize
    data object None: DiagnosisState()

    /**
     * Currently diagnosticating, a diagnosis has been selected
     */
    @Parcelize
    data class OnDiagnosis(val id: UUID): DiagnosisState()

}