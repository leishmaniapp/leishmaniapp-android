package com.leishmaniapp.entities.mock

import com.leishmaniapp.entities.Coordinates
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.DocumentType
import com.leishmaniapp.entities.IdentificationDocument
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ModelDiagnosticElement
import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Patient
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.SpecialistDiagnosticElement
import com.leishmaniapp.entities.Username
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

        fun mockImage(processed: Boolean = Random.nextBoolean()) = Image(
            date = faker.date.forward(1).toInstant().toKotlinInstant()
                .toLocalDateTime(
                    TimeZone.UTC
                ),
            width = 1024,
            height = 1024,
            processed = processed,
            sample = Random.nextInt(150),
            elements = setOf(
                mockSpecialistDiagnosticElement(),
                mockModelDiagnosticElement(),
            )
        )

        fun mockSpecialistDiagnosticElement() =
            SpecialistDiagnosticElement(
                name = MockDisease.diagnosticElements.random(),
                amount = Random.nextInt(50)
            )

        fun mockModelDiagnosticElement() =
            ModelDiagnosticElement(
                name = MockDisease.diagnosticElements.random(),
                model = MockDisease.models.random(),
                coordinates = buildSet {
                    repeat(Random.nextInt(5) + 5) {
                        this.add(
                            Coordinates(
                                x = Random.nextInt(2250),
                                y = Random.nextInt(2250)
                            )
                        )
                    }
                }.toMutableSet()
            )

        /**
         * Generate random mock diagnosis
         * @param isCompleted NULL for random image completion, else for completion status
         */
        fun mockDiagnosis(isCompleted: Boolean? = null) =
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
                disease = MockDisease,
                images = buildMap {
                    repeat(10) { idx ->
                        put(
                            idx,
                            if (isCompleted == null) {
                                mockImage()
                            } else {
                                mockImage(processed = isCompleted)
                            }
                        )
                    }
                }
            )
    }
}