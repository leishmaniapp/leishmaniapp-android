package com.leishmaniapp.infrastructure.persistance

import android.net.Uri
import androidx.room.TypeConverter
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.entities.DocumentType
import com.leishmaniapp.utilities.time.fromUnixToLocalDateTime
import com.leishmaniapp.utilities.time.toUnixTime
import kotlinx.datetime.LocalDateTime
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
}