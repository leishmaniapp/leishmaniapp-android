package com.leishmaniapp.entities.mock

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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import net.datafaker.Faker
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MockGenerator {
    companion object {
        // Faker instance
        private val faker = Faker()

        fun mockSpecialist() = Specialist(
            name = faker.name().fullName(),
            username = Username(faker.name().username()),
            password = Password("!")
        )

        fun mockPatient() = Patient(
            name = faker.name().fullName(),
            id = IdentificationDocument(faker.idNumber().peselNumber()),
            documentType = DocumentType.CC
        )

        fun mockImage(processed: Boolean = Random.nextBoolean()) = Image(
            date = faker.date().past(1, TimeUnit.DAYS).toInstant().toKotlinInstant()
                .toLocalDateTime(
                    TimeZone.UTC
                ),
            width = 1024,
            height = 1024,
            processed = processed,
            sample = Random.nextInt(),
            diagnosticElements = listOf(
                List(10) {
                    mockSpecialistDiagnosticElement()
                },
                List(10) {
                    mockModelDiagnosticElement()
                }
            ).flatten().toMutableList()
        )

        fun mockSpecialistDiagnosticElement() =
            SpecialistDiagnosticElement(
                name = "diagnostic.mock.element",
                amount = Random.nextInt(50)
            )

        fun mockModelDiagnosticElement() =
            ModelDiagnosticElement(
                name = "diagnostic.mock.element",
                diagnosisModel = MockDisease.models.random(),
                items = List(Random.nextInt(10)) {
                    Random.nextInt(10) to (Random.nextInt(10) + 10)
                }
            )

        fun mockDiagnosis() =
            Diagnosis(
                specialistResult = Random.nextBoolean(),
                modelResult = Random.nextBoolean(),
                date = faker.date().past(1, TimeUnit.DAYS).toInstant().toKotlinInstant()
                    .toLocalDateTime(
                        TimeZone.UTC
                    ),
                remarks = faker.lorem().paragraph(),
                specialist = mockSpecialist(),
                patientDiagnosed = mockPatient(),
                diagnosticDisease = MockDisease,
                diagnosticImages = buildSet {
                    repeat(10) {
                        add(mockImage())
                    }
                }.toMutableSet()
            )

    }
}