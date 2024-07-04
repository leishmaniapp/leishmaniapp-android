package com.leishmaniapp.domain.protobuf

import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.ImageMetadata
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.infrastructure.security.encodeBase64
import com.leishmaniapp.utilities.time.toUnixTime

fun ImageMetadata.toProto(): com.leishmaniapp.cloud.model.ImageMetadata =
    com.leishmaniapp.cloud.model.ImageMetadata(
        diagnosis = diagnosis.toString(),
        sample = sample,
        disease = disease.id,
        date = date.toUnixTime(),
    )

fun Diagnosis.toProto(): com.leishmaniapp.cloud.model.Diagnosis =
    com.leishmaniapp.cloud.model.Diagnosis(
        id = id.toString(),
        disease = disease.id,
        specialist = specialist.toProto(),
        results = results.toProto(),
        date = date.toUnixTime(),
        patient_hash = patient.hash.encodeBase64(),
        remarks = remarks,
        samples = samples,
    )

fun Diagnosis.Results.toProto(): com.leishmaniapp.cloud.model.Diagnosis.Results =
    com.leishmaniapp.cloud.model.Diagnosis.Results(specialist_result = specialistResult,
        specialist_elements = specialistElements.mapKeys { entry -> entry.key.id },
        model_result = modelResult,
        model_elements = modelElements.mapKeys { entry -> entry.key.id })

fun Specialist.toProto(): com.leishmaniapp.cloud.model.Specialist =
    com.leishmaniapp.cloud.model.Specialist(
        name = this.name,
        email = this.email,
        diseases = this.diseases.map { it.id },
    )

fun Specialist.Record.toProto(): com.leishmaniapp.cloud.model.Specialist.Record =
    com.leishmaniapp.cloud.model.Specialist.Record(
        email = email,
        name = name,
    )