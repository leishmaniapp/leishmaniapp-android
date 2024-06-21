package com.leishmaniapp.domain.entities

import android.os.Parcelable

sealed class DiagnosticElement : Parcelable {

    companion object;
    
    /**
     * Identificator for the [DiagnosticElement]
     */
    abstract val id: DiagnosticElementName

    /**
     * Number of elements detected
     */
    abstract val amount: Int

}