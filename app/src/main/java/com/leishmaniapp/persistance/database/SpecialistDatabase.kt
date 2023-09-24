package com.leishmaniapp.persistance.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leishmaniapp.entities.SpecialistRoom
import com.leishmaniapp.persistance.dao.SpecialistDao

@Database(
    entities = [SpecialistRoom::class],
    version = 1
)
abstract class SpecialistDatabase : RoomDatabase(){
    abstract fun specialistDao(): SpecialistDao
}