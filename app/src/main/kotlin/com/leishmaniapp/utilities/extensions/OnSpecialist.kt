package com.leishmaniapp.utilities.extensions

import com.leishmaniapp.domain.entities.Specialist

/**
 * Transform a [Specialist] into a [Specialist.Record]
 */
fun Specialist.toRecord(): Specialist.Record =
    Specialist.Record(
        email = email,
        name = name,
    )

/**
 * Transform a [com.leishmaniapp.cloud.model.Specialist] into a [com.leishmaniapp.cloud.model.Specialist.Record]
 */
fun com.leishmaniapp.cloud.model.Specialist.toRecord(): com.leishmaniapp.cloud.model.Specialist.Record =
    com.leishmaniapp.cloud.model.Specialist.Record(
        name = name,
        email = email
    )