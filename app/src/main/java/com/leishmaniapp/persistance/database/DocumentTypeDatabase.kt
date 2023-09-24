package com.leishmaniapp.persistance.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.leishmaniapp.entities.DocumentTypeConverter
import com.leishmaniapp.entities.DocumentTypeRoom
import com.leishmaniapp.persistance.dao.DocumentTypeDao

@Database(
    entities = [DocumentTypeRoom::class],
    version = 1)
@TypeConverters(DocumentTypeConverter::class)
abstract class DocumentTypeDatabase : RoomDatabase(){
    abstract fun documentTypeDao() : DocumentTypeDao
}