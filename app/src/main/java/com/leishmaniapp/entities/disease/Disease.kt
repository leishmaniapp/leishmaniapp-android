package com.leishmaniapp.entities.disease

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.DiagnosticElementName
import com.leishmaniapp.usecases.serialization.ParentDiseaseSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable(with = ParentDiseaseSerializer::class)
sealed class Disease(
    val id: String,
    val models: Set<DiagnosisModel>,
    val elements: Set<DiagnosticElementName>
) : Parcelable {
    abstract val displayName: String
        @Composable get

    abstract val painterResource: Painter
        @Composable get

    companion object {
        fun diseases(): List<Disease> =
            Disease::class.sealedSubclasses.map { it.objectInstance!! }.toList()

        fun where(id: String): Disease? =
            Disease::class.sealedSubclasses.firstOrNull { it.objectInstance?.id == id }?.objectInstance
    }
}
