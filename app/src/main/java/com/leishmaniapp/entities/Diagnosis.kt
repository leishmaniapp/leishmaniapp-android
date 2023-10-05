package com.leishmaniapp.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.leishmaniapp.entities.disease.Disease
import com.leishmaniapp.usecases.types.LocalDateTimeTypeParceler
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID
import kotlin.reflect.KClass

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Specialist::class,
            childColumns = ["specialistUsername"],
            parentColumns = ["username"]
        ),
        ForeignKey(
            entity = Patient::class,
            childColumns = ["patientIdDocument", "patientIdType"],
            parentColumns = ["id", "documentType"]
        ),
    ]
)
data class DiagnosisRoom(
    @PrimaryKey val id: @Serializable(UUIDSerializer::class) UUID = UUID.randomUUID(),
    val specialistResult: Boolean,
    val modelResult: Boolean,
    val date: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val remarks: String?,
    val specialistUsername: Username,
    val patientIdDocument: IdentificationDocument,
    val patientIdType: DocumentType,
    val disease: Disease,
    val images: Map<Int, Image>
) {
    companion object {
        fun Diagnosis.asRoomEntity(): DiagnosisRoom = DiagnosisRoom(
            id,
            specialistResult,
            modelResult,
            date,
            remarks,
            specialist.username,
            patient.id,
            patient.documentType,
            disease,
            images
        )
    }
}

/**
 * Class representing a Diagnosis
 * @immutable Replace by using [Diagnosis.copy]
 */
@Serializable
@Parcelize
data class Diagnosis(
    val id: @Serializable(UUIDSerializer::class) UUID = UUID.randomUUID(),
    val specialistResult: Boolean,
    val modelResult: Boolean,
    @TypeParceler<LocalDateTime, LocalDateTimeTypeParceler>
    val date: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val remarks: String?,
    val specialist: Specialist,
    val patient: Patient,
    val disease: Disease,
    val images: Map<Int, Image>,
) : Parcelable {
    /**
     * Group [DiagnosticElement] in a map in which the key is the [DiagnosticElementName] and the
     * value is another map in which the key is the element type (either [ModelDiagnosticElement]
     * or [SpecialistDiagnosticElement], and the value is the number of those elements found
     * @TODO Write tests for this function
     */
    @IgnoredOnParcel
    val computedResults: Map<DiagnosticElementName, Map<KClass<out DiagnosticElement>, Int>> by lazy {
        images.values.flatMap {
            it.elements
        }
            .groupBy { it.name }
            .mapValues {
                it.value.map { diagnosticElement ->
                    diagnosticElement::class to diagnosticElement.amount
                }.groupingBy { elementPair ->
                    elementPair.first
                }.aggregate { _, accumulator: Int?, element, _ ->
                    accumulator?.plus(element.second) ?: element.second
                }
            }
    }

    /**
     * Get the amount of images associated with the diagnosis
     * @TODO Write tests for this function
     */
    val samples: Int
        get() = images.size

    /**
     * Checks if every image in the diagnosis has been processed
     * @TODO Write tests for this function
     */
    val completed: Boolean
        get() = !images.any { !it.value.processed }
}

/**
 * Serializer for the [Diagnosis.id]
 */
object UUIDSerializer : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): UUID = UUID.fromString(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString())
}
