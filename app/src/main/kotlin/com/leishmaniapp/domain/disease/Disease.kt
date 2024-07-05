package com.leishmaniapp.domain.disease

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.DiagnosticElementName
import com.leishmaniapp.domain.entities.DiagnosticModel
import com.leishmaniapp.domain.serialization.DiseaseSerializer
import com.leishmaniapp.domain.types.ComputedResultsType
import com.leishmaniapp.domain.types.Identificator
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Represents a [Disease] and all the associated [DiagnosticModel], every new disease
 * must inherit from this class as an object
 */
@Parcelize
@Serializable(with = DiseaseSerializer::class)
sealed class Disease(

    /**
     * Must be the same within the application and the cloud
     */
    val id: Identificator,

    /**
     * Required image size for analysis, image aspect ratio is always 1:1 so this size represents
     * both height and width
     */
    val crop: Int,

    /**
     * List of [DiagnosticModel] associated to the disease
     */
    val models: Set<DiagnosticModel>,

    /**
     * List of [DiagnosticElementName] associated to the disease
     */
    val elements: Set<DiagnosticElementName>,

    /**
     * [R.string] resource representing the intl disease name
     */
    @Transient val displayNameResource: Int = R.string.unknown_disease,

    /**
     * [R.drawable] resource with the disease icon in menu
     */
    @Transient val painterResource: Int = R.drawable.macrophage

) : Parcelable {

    /**
     * Display name for the disease
     */
    val displayName: String
        @Composable get() = stringResource(id = displayNameResource)

    /**
     * Icon to show within the diseases menu
     */
    val painter: Painter
        @Composable get() = painterResource(id = painterResource)

    companion object {
        /**
         * List of diseases that inherit from this disease
         */
        fun diseases(): Set<Disease> =
            Disease::class.sealedSubclasses.filter { it.objectInstance != null }
                .map { it.objectInstance!! }.toSet()

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
