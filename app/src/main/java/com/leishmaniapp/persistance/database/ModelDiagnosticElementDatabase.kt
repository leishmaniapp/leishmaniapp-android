package com.leishmaniapp.persistance.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leishmaniapp.entities.ModelDiagnosticElementRoom
import com.leishmaniapp.persistance.dao.ModelDiagnosticElementDao

@Database(
    entities = [ModelDiagnosticElementRoom::class],
    version = 1
    )
abstract class ModelDiagnosticElementDatabase : RoomDatabase(){
    abstract fun modelDiagnosticElementDao(): ModelDiagnosticElementDao
}