package com.leishmaniapp.utils

import com.leishmaniapp.entities.Coordinates
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.DocumentType
import com.leishmaniapp.entities.IdentificationDocument
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageAnalysisStatus
import com.leishmaniapp.entities.ModelDiagnosticElement
import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Patient
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.SpecialistDiagnosticElement
import com.leishmaniapp.entities.Username
import com.leishmaniapp.entities.disease.MockDisease
import io.bloco.faker.Faker
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random

class MockGenerator {
    companion object {
        // Faker instance
        private val faker = Faker()

        fun mockSpecialist() = Specialist(
            name = faker.name.name(),
            username = Username(faker.internet.userName()),
            password = Password("!")
        )

        fun mockPatient() = Patient(
            name = faker.name.name(),
            id = IdentificationDocument(faker.phoneNumber.phoneNumber()),
            documentType = DocumentType.CC
        )

        fun mockImage(
            processed: ImageAnalysisStatus = ImageAnalysisStatus.entries.toTypedArray().random(),
            size: Int = 2250,
            sample: Int = Random.nextInt(150),
        ) = Image(
            sample = sample,
            date = faker.date.forward(1).toInstant().toKotlinInstant()
                .toLocalDateTime(
                    TimeZone.UTC
                ),
            size = size,
            processed = processed,
            elements = if (processed == ImageAnalysisStatus.Analyzed) {
                setOf(
                    mockSpecialistDiagnosticElement(),
                    mockModelDiagnosticElement(size)
                )
            } else {
                setOf(mockSpecialistDiagnosticElement())
            }
        )

        fun mockSpecialistDiagnosticElement() =
            SpecialistDiagnosticElement(
                name = MockDisease.elements.random(),
                amount = Random.nextInt(50)
            )

        fun mockModelDiagnosticElement(imageSize: Int = 2250) =
            ModelDiagnosticElement(
                name = MockDisease.elements.random(),
                model = MockDisease.models.random(),
                coordinates = buildSet {
                    repeat(Random.nextInt(5) + 5) {
                        this.add(
                            Coordinates(
                                x = Random.nextInt(imageSize),
                                y = Random.nextInt(imageSize)
                            )
                        )
                    }
                }.toMutableSet()
            )

        /**
         * Generate random mock diagnosis
         * @param isCompleted NULL for random image completion, else for completion status
         */
        fun mockDiagnosis(isCompleted: Boolean? = null, isFinished: Boolean = false) =
            Diagnosis(
                specialistResult = Random.nextBoolean(),
                modelResult = Random.nextBoolean(),
                date = faker.date.forward(1).toInstant().toKotlinInstant()
                    .toLocalDateTime(
                        TimeZone.UTC
                    ),
                remarks = faker.lorem.paragraph(),
                specialist = mockSpecialist(),
                patient = mockPatient(),
                finalized = isFinished,
                disease = MockDisease,
                images = buildMap {
                    repeat(10) { idx ->
                        put(
                            idx,
                            if (isCompleted == null) {
                                mockImage()
                            } else {
                                mockImage(processed = ImageAnalysisStatus.Analyzed)
                            }
                        )
                    }
                }
            )
    }
}