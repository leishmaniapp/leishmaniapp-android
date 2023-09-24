package com.leishmaniapp.persistance.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leishmaniapp.entities.IdentificationDocumentRoom
import com.leishmaniapp.persistance.dao.IdentificationDocumentDao

@Database(
    entities = [IdentificationDocumentRoom::class],
    version = 1
)
abstract class IdentificationDocumentDatabase : RoomDatabase(){
    abstract fun identificationDocumentDao(): IdentificationDocumentDao
}