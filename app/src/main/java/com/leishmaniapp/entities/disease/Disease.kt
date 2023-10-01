package com.leishmaniapp.entities.disease

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.DiagnosticElementName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Entity
data class DiseaseRoom(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val models: String,
    val elements: String
)

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
}

object ParentDiseaseSerializer : KSerializer<Disease> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Disease", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Disease) = encoder.encodeString(value.id)
    override fun deserialize(decoder: Decoder): Disease {
        val diseaseId = decoder.decodeString()
        try {
            return Disease::class.sealedSubclasses
                .map { it.objectInstance }
                .first {
                    it?.id == diseaseId
                }!!
        } catch (_: NoSuchElementException) {
            throw SerializationException("Disease does not exist or not supported")
        }
    }
}

abstract class DiseaseSerializer<T>(serialName: String) : KSerializer<T>
        where T : Disease {

    override val descriptor = PrimitiveSerialDescriptor(serialName, PrimitiveKind.STRING)

    val diseaseDelegatedSerializer = ParentDiseaseSerializer
    override fun serialize(encoder: Encoder, value: T) =
        encoder.encodeSerializableValue(diseaseDelegatedSerializer, value as Disease)

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(decoder: Decoder): T =
        decoder.decodeSerializableValue(diseaseDelegatedSerializer) as T
}
