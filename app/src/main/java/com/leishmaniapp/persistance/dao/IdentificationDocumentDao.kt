package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.IdentificationDocumentRoom

@Dao
interface IdentificationDocumentDao {
    @Upsert
    suspend fun upsertIdentificationDocument(identificationDocument: IdentificationDocumentRoom)

    @Delete
    suspend fun deleteIdentificationDocument(identificationDocument: IdentificationDocumentRoom)

    @Query("SELECT * FROM identificationdocumentroom ")
    suspend fun getIdentificationDocument(value: String): List<IdentificationDocumentRoom>
}