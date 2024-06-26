package com.leishmaniapp.utilities.mock

import android.graphics.ColorSpace.Model
import com.leishmaniapp.domain.calibration.ImageCalibrationData
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.disease.LeishmaniasisGiemsaDisease
import com.leishmaniapp.domain.disease.MockDotsDisease
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.DiagnosticElement
import com.leishmaniapp.domain.entities.DocumentType
import com.leishmaniapp.domain.entities.ImageMetadata
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.entities.ModelDiagnosticElement
import com.leishmaniapp.domain.entities.Patient
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.domain.entities.SpecialistDiagnosticElement
import com.leishmaniapp.domain.types.Coordinates
import com.leishmaniapp.infrastructure.camera.computeMichelsonContrast
import com.leishmaniapp.infrastructure.camera.computeSimpleLuminance
import com.leishmaniapp.utilities.extensions.toRecord
import com.leishmaniapp.utilities.extensions.utcNow
import com.leishmaniapp.utilities.mock.MockGenerator.mock
import com.leishmaniapp.utilities.types.Triplet
import io.bloco.faker.Faker
import kotlinx.datetime.LocalDateTime
import java.util.UUID

/**
 * Generate mock values for application entities
 */
object MockGenerator {
    /**
     * Faker instance for generating fake data
     */
    private val faker = Faker()

    /**
     * Generate fake [ImageCalibrationData.HSV] values
     */
    fun ImageCalibrationData.HSV.Companion.mock(): ImageCalibrationData.HSV =
        ImageCalibrationData.HSV(
            hue = faker.number.between(0.0, 1.0).toFloat(),
            saturation = faker.number.between(0.0, 1.0).toFloat(),
            value = faker.number.between(0.0, 1.0).toFloat(),
        )

    /**
     * Generate random data for [ImageCalibrationData]
     */
    fun ImageCalibrationData.Companion.mock(): ImageCalibrationData = Triplet(
        ImageCalibrationData.HSV.mock(),
        ImageCalibrationData.HSV.mock(),
        ImageCalibrationData.HSV.mock()
    ).let { hsv ->
        ImageCalibrationData(
            megapixels = faker.number.between(1.0, 50.0),
            hsv = hsv,
            luminance = hsv.computeSimpleLuminance(),
            contrast = hsv.computeMichelsonContrast()
        )
    }

    /**
     * Generate a random data [Specialist]
     */
    fun Specialist.Companion.mock() = Specialist(
        name = faker.name.name(),
        email = faker.internet.email(),
        token = faker.internet.password(),
        diseases = Disease.diseases(),
    )

    /**
     * Get a random value for [DocumentType]
     */
    private fun DocumentType.Companion.random() = DocumentType.entries.random()

    /**
     * Generate a random data [Patient]
     */
    fun Patient.Companion.mock() = Patient(
        name = faker.name.name(),
        id = faker.number.between(100_000_000, 999_999_999).toString(),
        documentType = DocumentType.random(),
    )

    /**
     * Get a random value for [AnalysisStage]
     */
    private fun AnalysisStage.Companion.random() = AnalysisStage.entries.random();

    /**
     * Generate a random data [ImageMetadata]
     */
    private fun ImageMetadata.Companion.mock(sample: Int) = ImageMetadata(
        diagnosis = UUID.randomUUID(),
        sample = sample,
        disease = setOf(LeishmaniasisGiemsaDisease, MockDotsDisease).random(),
        date = LocalDateTime.utcNow(),
    )

    /**
     * Generate a random data [SpecialistDiagnosticElement]
     */
    fun SpecialistDiagnosticElement.Companion.mock(): DiagnosticElement =
        SpecialistDiagnosticElement(
            id = MockDotsDisease.elements.random(),
            amount = faker.number.positive(0, 12)
        )

    /**
     * Create a [Coordinates] with given [min] and [max] values
     */
    private fun Coordinates.Companion.mock(min: Int, max: Int) = Coordinates(
        x = faker.number.between(min, max),
        y = faker.number.between(min, max),
    )

    /**
     * Create a [ModelDiagnosticElement] for an image with a given [imageSize]
     */
    fun ModelDiagnosticElement.Companion.mock(
        imageSize: Int = faker.number.between(
            250,
            2000
        )
    ): DiagnosticElement =
        ModelDiagnosticElement(
            id = MockDotsDisease.elements.first(),
            model = MockDotsDisease.models.first(),
            coordinates = buildSet {
                repeat(faker.number.positive(3, 12)) {
                    add(Coordinates.mock(0, imageSize))
                }
            }
        )

    /**
     * Generate a random data [ImageSample]
     */
    fun ImageSample.Companion.mock(
        sample: Int = faker.number.between(0, 100),
        stage: AnalysisStage = AnalysisStage.random(),
    ) = ImageSample(
        metadata = ImageMetadata.mock(sample),
        stage = stage,
        elements = if (stage == AnalysisStage.Analyzed) {
            setOf(
                SpecialistDiagnosticElement.mock(),
                ModelDiagnosticElement.mock(),
            )
        } else {
            setOf(
                SpecialistDiagnosticElement.mock()
            )
        }
    )

    /**
     * Generate [Diagnosis.Results] data
     */
    private fun Diagnosis.Results.Companion.mock() = Diagnosis.Results()

    /**
     * Generate random mock diagnosis
     * @param isCompleted null for random image completion, else for completion status
     */
    fun Diagnosis.Companion.mock(
        isCompleted: Boolean = faker.bool.bool(),
        isFinished: Boolean = false
    ) =
        Diagnosis(
            results = Diagnosis.Results.mock(),
            date = LocalDateTime.utcNow(),
            remarks = faker.lorem.paragraph(),
            specialist = Specialist.mock().toRecord(),
            patient = Patient.mock(),
            finalized = isFinished,
            disease = MockDotsDisease,
            images = buildList {
                repeat(faker.number.between(0, 10)) { idx ->
                    add(
                        if (isCompleted) {
                            ImageSample.mock(sample = idx, stage = AnalysisStage.Analyzed)
                        } else {
                            ImageSample.mock(sample = idx)
                        }
                    )
                }
            }
        )
}