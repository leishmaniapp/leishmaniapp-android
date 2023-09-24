package com.leishmaniapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

/**
 * Document identification type
 */
enum class DocumentType {
    CC,
    TI,
    CE,
    PASSPORT
}

class DocumentTypeConverter {
    @TypeConverter
    fun fromDocumentType(documentType: DocumentType): String {
        return documentType.name
    }

    @TypeConverter
    fun toDocumentType(documentTypeString: String): DocumentType {
        return enumValueOf(documentTypeString)
    }
}

@Entity
@TypeConverters(DocumentTypeConverter::class)
data class DocumentTypeRoom(
    @PrimaryKey(autoGenerate = false)
    val documentType: DocumentType
)