package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.ImageRoom

@Dao
interface ImageDao {

    @Upsert
    suspend fun upsertImage(image: ImageRoom)

    @Delete
    suspend fun deleteImage(image: ImageRoom)

    @Query("SELECT * FROM imageroom")
    suspend fun getImages(): List<ImageRoom>

}