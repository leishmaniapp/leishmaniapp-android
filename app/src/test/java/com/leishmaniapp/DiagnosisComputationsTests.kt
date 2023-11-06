package com.leishmaniapp

import com.leishmaniapp.entities.Coordinates
import com.leishmaniapp.entities.ImageAnalysisStatus
import com.leishmaniapp.entities.ModelDiagnosticElement
import com.leishmaniapp.entities.SpecialistDiagnosticElement
import com.leishmaniapp.entities.disease.MockDisease
import com.leishmaniapp.utils.MockGenerator
import org.junit.Assert
import org.junit.Test

class DiagnosisComputationsTests {
    @Test
    fun computedResultsTest() {
        val images = listOf(
            MockGenerator.mockImage().copy(
                elements = setOf(
                    // 3 Model diagnostic elements
                    ModelDiagnosticElement(
                        MockDisease.elements.first(), MockDisease.models.first(), setOf(
                            Coordinates(0, 0),
                            Coordinates(1, 1),
                            Coordinates(2, 2),
                        )
                    ),
                    // 2 Specialist diagnostic elements
                    SpecialistDiagnosticElement(
                        MockDisease.elements.first(), 2
                    )
                )
            ), MockGenerator.mockImage().copy(
                elements = setOf(
                    // 1 Model diagnostic elements
                    ModelDiagnosticElement(
                        MockDisease.elements.first(), MockDisease.models.first(), setOf(
                            Coordinates(0, 0),
                        )
                    ),
                    // 3 Specialist diagnostic elements
                    SpecialistDiagnosticElement(
                        MockDisease.elements.first(), 3
                    )
                )
            )
        )

        val diagnosis = MockGenerator.mockDiagnosis()
            .copy(disease = MockDisease, images = images.associateBy { it.sample })

        val computedElements = diagnosis.computedResults

        Assert.assertEquals(
            computedElements[MockDisease.elements.first()]?.get(
                ModelDiagnosticElement::class
            ), 4
        )

        Assert.assertEquals(
            computedElements[MockDisease.elements.first()]?.get(
                SpecialistDiagnosticElement::class
            ), 5
        )
    }

    @Test
    fun diagnosisResultWithComputedResultsForDisease() {
        val images = listOf(
            MockGenerator.mockImage().copy(
                elements = setOf(
                    // 3 Model diagnostic elements
                    ModelDiagnosticElement(
                        MockDisease.elements.first(),
                        MockDisease.models.first(),
                        List(10) { counter ->
                            Coordinates(counter, counter)
                        }.toSet()
                    ),
                )
            )
        )

        val diagnosis = MockGenerator.mockDiagnosis()
            .copy(disease = MockDisease, images = images.associateBy { it.sample })

        Assert.assertEquals(
            diagnosis.withModelResult().modelResult,
            MockDisease.computeDiagnosisResult(diagnosis.computedResults)
        )
    }

    @Test
    fun diagnosisSamplesTest() {
        val images = List(10) { counter -> MockGenerator.mockImage().copy(sample = counter) }
        val diagnosis =
            MockGenerator.mockDiagnosis().copy(images = images.associateBy { it.sample })

        Assert.assertEquals(diagnosis.samples, images.size)
    }

    @Test
    fun diagnosisIsCompletedTest() {
        var diagnosis = MockGenerator.mockDiagnosis()
        val images = List(10) { counter ->
            MockGenerator.mockImage()
                .copy(sample = counter, processed = ImageAnalysisStatus.Analyzed)
        }.toMutableList()

        diagnosis = diagnosis.copy(images = images.associateBy { it.sample })
        Assert.assertTrue(diagnosis.completed)

        images[0] = images[0].copy(processed = ImageAnalysisStatus.NotAnalyzed)
        diagnosis = diagnosis.copy(images = images.associateBy { it.sample })
        Assert.assertFalse(diagnosis.completed)
    }

    @Test
    fun diagnosisAppendImageTest() {
        var diagnosis = MockGenerator.mockDiagnosis()
        val images = List(10) { counter -> MockGenerator.mockImage().copy(sample = counter) }

        Assert.assertEquals(
            diagnosis.samples,
            images.size
        )

        diagnosis = diagnosis.appendImage(MockGenerator.mockImage())
        Assert.assertEquals(
            diagnosis.samples,
            images.size + 1
        )
    }
}