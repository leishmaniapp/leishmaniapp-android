package com.leishmaniapp.persistance.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leishmaniapp.entities.CoordinatesRoom
import com.leishmaniapp.persistance.dao.CoordinatesDao

@Database(
    entities = [CoordinatesRoom::class],
    version = 1
)
abstract class CoordinatesDatabase : RoomDatabase(){
    abstract fun coordinatesDao() : CoordinatesDao
}