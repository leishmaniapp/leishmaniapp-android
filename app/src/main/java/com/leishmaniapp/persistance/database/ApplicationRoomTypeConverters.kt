package com.leishmaniapp.persistance.database

import android.net.Uri
import androidx.room.TypeConverter
import com.leishmaniapp.entities.DiagnosticElement
import com.leishmaniapp.entities.DocumentType
import com.leishmaniapp.entities.IdentificationDocument
import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Username
import com.leishmaniapp.entities.disease.Disease
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

class ApplicationRoomTypeConverters {

    /* Username */
    @TypeConverter
    fun usernameToString(username: Username): String = username.value

    @TypeConverter
    fun stringToUsername(string: String): Username = Username(string)

    /* Password */
    @TypeConverter
    fun passwordToString(password: Password): String = password.value

    @TypeConverter
    fun stringToPassword(string: String): Password = Password(string)

    /* IdentificationDocument */
    @TypeConverter
    fun identificationDocumentToString(id: IdentificationDocument): String = id.value

    @TypeConverter
    fun stringToIdentificationDocument(id: String): IdentificationDocument =
        IdentificationDocument(id)

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
    fun stringToDisease(id: String): Disease? = Disease.where(id);

    /* Set<DiagnosticElement> */
    @TypeConverter
    fun diagnosticElementSetToJson(diagnosticElements: Set<DiagnosticElement>): String =
        Json.encodeToString(diagnosticElements)

    @TypeConverter
    fun jsonToDiagnosticElementSet(diagnosticElements: String): Set<DiagnosticElement> =
        Json.decodeFromString(diagnosticElements)

    /* LocalDateTime */
    @TypeConverter
    fun localDateTimeToString(time: LocalDateTime) = time.toString()

    @TypeConverter
    fun stringToLocalDateTime(value: String) = LocalDateTime.parse(value)

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