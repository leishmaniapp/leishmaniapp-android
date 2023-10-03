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