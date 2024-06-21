package com.leishmaniapp.infrastructure.persistance

import android.net.Uri
import androidx.room.TypeConverter
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.entities.DiagnosticElement
import com.leishmaniapp.domain.entities.DiagnosticElementName
import com.leishmaniapp.domain.entities.DocumentType
import com.leishmaniapp.utilities.time.fromUnixToLocalDateTime
import com.leishmaniapp.utilities.time.toUnixTime
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

/**
 * Serialize data into Room database
 */
class ApplicationRoomTypeConverters {

    /* DocumentType */
    @TypeConverter
    fun documentTypeToString(documentType: DocumentType): String = documentType.name

    @TypeConverter
    fun stringToDocumentType(documentTypeString: String): DocumentType =
        enumValueOf(documentTypeString)

    /* Disease */
    @TypeConverter
    fun diseaseToString(disease: Disease): String = disease.id

    @TypeConverter
    fun stringToDisease(id: String): Disease? = Disease.diseaseById(id)

    /* LocalDateTime */
    @TypeConverter
    fun localDateTimeToUnix(time: LocalDateTime): Long = time.toUnixTime()

    @TypeConverter
    fun unixToLocalDateTime(value: Long) = value.fromUnixToLocalDateTime()

    /* UUID */
    @TypeConverter
    fun uuidToString(uuid: UUID): String = uuid.toString()

    @TypeConverter
    fun stringToUuid(value: String): UUID = UUID.fromString(value)

    /* Uri */
    @TypeConverter
    fun uriToString(uri: Uri): String = uri.path!!

    @TypeConverter
    fun stringToUri(value: String): Uri = Uri.parse(value)

    /* Set<Disease> */
    @TypeConverter
    fun diseasesSetToJson(diseases: Set<Disease>): String = Json.encodeToString(diseases)

    @TypeConverter
    fun jsonToDiseasesSet(diseasesJson: String): Set<Disease> = Json.decodeFromString(diseasesJson)

    /* Map<DiagnosticElementName, Int> */
    @TypeConverter
    fun mapDiagnosticElementNameIntToJson(value: Map<DiagnosticElementName, Int>): String =
        Json.encodeToString(value)

    @TypeConverter
    fun jsonToMapDiagnosticElementNameInt(value: String): Map<DiagnosticElementName, Int> =
        Json.decodeFromString(value)

    /* Set<DiagnosticElement> */
    @TypeConverter
    fun diagnosticElementSetToJson(diagnosticElements: Set<DiagnosticElement>): String =
        Json.encodeToString(diagnosticElements)

    @TypeConverter
    fun jsonToDiagnosticElementSet(diagnosticElements: String): Set<DiagnosticElement> =
        Json.decodeFromString(diagnosticElements)
}