package com.leishmaniapp.domain.disease

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.DiagnosticElementName
import com.leishmaniapp.domain.entities.DiagnosticModel
import com.leishmaniapp.domain.serialization.DiseaseSerializer
import com.leishmaniapp.domain.types.ComputedResultsType
import com.leishmaniapp.domain.types.Identificator
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable(with = DiseaseSerializer::class)
sealed class Disease(
    val id: Identificator,
    val models: Set<DiagnosticModel>,
    val elements: Set<DiagnosticElementName>,
    @Transient val displayNameResource: Int = R.string.unknown_disease,
) : Parcelable {

    /**
     * Display name for the disease
     */
    val displayName: String
        @Composable get() = stringResource(id = displayNameResource)

    /**
     * Icon to show within the diseases menu
     */
    abstract val painterResource: Painter
        @Composable get

    companion object {
        /**
         * List of diseases that inherit from this disease
         */
        fun diseases(): Set<Disease> =
            Disease::class.sealedSubclasses.map { it.objectInstance!! }.toSet()

        /**
         * Get a disease that inherits [Disease] with the given id
         */
        fun diseaseById(id: Identificator): Disease? =
            Disease::class.sealedSubclasses.firstOrNull { it.objectInstance?.id == id }?.objectInstance
    }

    /**
     * Result of the diagnosis Positive or Negative given the amount and type of elements
     * found during the diagnosis, each disease must implement its own way of computing a result
     */
    abstract fun modelResultForDisease(computedResults: ComputedResultsType): Boolean
}
