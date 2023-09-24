package com.leishmaniapp.persistance.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leishmaniapp.entities.ImageRoom
import com.leishmaniapp.persistance.dao.ImageDao

@Database(
    entities = [ImageRoom::class],
    version = 1
)
abstract class ImageDatabase : RoomDatabase(){
    abstract fun imageDao(): ImageDao
}