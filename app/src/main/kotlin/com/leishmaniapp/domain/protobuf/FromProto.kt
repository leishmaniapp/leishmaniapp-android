package com.leishmaniapp.domain.protobuf

import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.DiagnosticElementName
import com.leishmaniapp.domain.entities.ImageMetadata
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.utilities.time.fromUnixToLocalDateTime
import java.util.UUID

fun ImageMetadata.Companion.fromProto(from: com.leishmaniapp.cloud.model.ImageMetadata): ImageMetadata =
    ImageMetadata(
        diagnosis = UUID.fromString(from.diagnosis),
        sample = from.sample,
        disease = Disease.diseaseById(from.disease)!!,
        date = from.date.fromUnixToLocalDateTime()
    )

fun Diagnosis.Results.Companion.fromProto(from: com.leishmaniapp.cloud.model.Diagnosis.Results): Diagnosis.Results =
    Diagnosis.Results(
        specialistResult = from.specialist_result,
        specialistElements = from.specialist_elements.mapKeys { entry ->
            DiagnosticElementName.diagnosticElementNameById(
                entry.key
            )
        },
        modelResult = from.model_result,
        modelElements = from.model_elements.mapKeys { entry ->
            DiagnosticElementName.diagnosticElementNameById(
                entry.key
            )
        },
    )

fun Specialist.Companion.fromProto(from: com.leishmaniapp.cloud.model.Specialist): Specialist =
    Specialist(
        name = from.name,
        email = from.email,
        diseases = from.diseases.map { Disease.diseaseById(it)!! }.toSet(),
    )

fun Specialist.Record.Companion.fromProto(from: com.leishmaniapp.cloud.model.Specialist.Record): Specialist.Record =
    Specialist.Record(
        email = from.email,
        name = from.name,
    )