package com.leishmaniapp.infrastructure.persistance.repository

import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.domain.repository.ISpecialistsRepository
import com.leishmaniapp.domain.types.AccessToken
import com.leishmaniapp.domain.types.Email
import com.leishmaniapp.infrastructure.persistance.dao.RoomSpecialistsDao
import com.leishmaniapp.infrastructure.persistance.entities.RoomSpecialistEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * [ISpecialistsRepository] for the Room database
 */
class RoomSpecialistRepositoryImpl @Inject constructor(
    /**
     * DAO for interaction with the Room database
     */
    val dao: RoomSpecialistsDao,

    ) : ISpecialistsRepository {
    override suspend fun upsertSpecialist(specialist: Specialist) =
        dao.upsertSpecialist(RoomSpecialistEntity(specialist))

    override suspend fun deleteSpecialist(specialist: Specialist) = dao.deleteSpecialist(
        RoomSpecialistEntity(specialist)
    )

    override fun specialistByEmail(email: Email): Flow<Specialist?> =
        dao.specialistByEmail(email).map { it?.toSpecialist() }

    override fun specialistByToken(token: AccessToken): Flow<Specialist?> =
        dao.specialistByToken(token).map { it?.toSpecialist() }

    override fun deleteSpecialistByEmail(email: Email) = dao.deleteSpecialistByEmail(email)

    override fun allSpecialists(): Flow<List<Specialist>> =
        dao.allSpecialists().map { flow -> flow.map { it.toSpecialist() } }
}