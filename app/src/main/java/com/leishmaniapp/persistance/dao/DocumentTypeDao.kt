package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.DocumentTypeRoom

@Dao
interface DocumentTypeDao {
    @Upsert
    suspend fun upsertDocumentType(documentType: DocumentTypeRoom)

    @Query("SELECT * FROM DocumentTypeRoom")
    suspend fun getDocumentType(): List<DocumentTypeRoom>
}