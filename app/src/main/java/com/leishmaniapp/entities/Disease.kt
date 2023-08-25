package com.leishmaniapp.entities

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
abstract class Disease(
    val id: String,
) {
    /**
     * JetpackCompose access to displayName
     */
    abstract val displayName: String
        @Composable get

    val models: MutableSet<DiagnosisModel> = mutableSetOf()
    fun modelById(id: String): DiagnosisModel? = models.find { it.model == id }
}
