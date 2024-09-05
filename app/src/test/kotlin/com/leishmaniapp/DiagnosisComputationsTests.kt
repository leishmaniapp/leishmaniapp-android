package com.leishmaniapp

import com.leishmaniapp.domain.disease.MockSpotsDisease
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.entities.ModelDiagnosticElement
import com.leishmaniapp.domain.entities.SpecialistDiagnosticElement
import com.leishmaniapp.domain.types.BoxCoordinates
import com.leishmaniapp.utilities.mock.MockGenerator.mock
import org.junit.Assert
import org.junit.Test

class DiagnosisComputationsTests {
    @Test
    fun computedResultsTest() {
        val images = listOf(
            ImageSample.mock().copy(
                elements = setOf(
                    // 3 Model diagnostic elements
                    ModelDiagnosticElement(
                        MockSpotsDisease.elements.first(),
                        "n/a",
                        setOf(
                            BoxCoordinates(0, 0),
                            BoxCoordinates(1, 1),
                            BoxCoordinates(2, 2),
                        )
                    ),
                    // 2 Specialist diagnostic elements
                    SpecialistDiagnosticElement(
                        MockSpotsDisease.elements.first(), 2
                    )
                )
            ), ImageSample.mock().copy(
                elements = setOf(
                    // 1 Model diagnostic elements
                    ModelDiagnosticElement(
                        MockSpotsDisease.elements.first(),
                        "n/a",
                        setOf(
                            BoxCoordinates(0, 0),
                        )
                    ),
                    // 3 Specialist diagnostic elements
                    SpecialistDiagnosticElement(
                        MockSpotsDisease.elements.first(), 3
                    )
                )
            )
        )

        val diagnosis = Diagnosis.mock()
            .copy(disease = MockSpotsDisease, images = images)

        val computedElements = diagnosis.computedResults

        Assert.assertEquals(
            computedElements[MockSpotsDisease.elements.first()]?.get(
                ModelDiagnosticElement::class
            ), 4
        )

        Assert.assertEquals(
            computedElements[MockSpotsDisease.elements.first()]?.get(
                SpecialistDiagnosticElement::class
            ), 5
        )
    }

    @Test
    fun diagnosisResultWithComputedResultsForDisease() {
        val images = listOf(
            ImageSample.mock().copy(
                elements = setOf(
                    // 3 Model diagnostic elements
                    ModelDiagnosticElement(
                        MockSpotsDisease.elements.first(),
                        "n/a",
                        List(10) { counter ->
                            BoxCoordinates(counter, counter)
                        }.toSet()
                    ),
                )
            )
        )

        val diagnosis = Diagnosis.mock()
            .copy(disease = MockSpotsDisease, images = images)

        Assert.assertEquals(
            diagnosis.withResults().results.modelResult,
            MockSpotsDisease.modelResultForDisease(diagnosis.computedResults)
        )
    }

    @Test
    fun diagnosisSamplesTest() {
        val images = List(10) { counter ->
            ImageSample.mock().run {
                copy(metadata = metadata.copy(sample = counter))
            }
        }
        val diagnosis =
            Diagnosis.mock().copy(images = images)

        Assert.assertEquals(diagnosis.samples, images.size)
    }

    @Test
    fun diagnosisIsCompletedTest() {
        // Create a diagnosis
        var diagnosis = Diagnosis.mock()

        // Create a list of analyzed images
        val images = List(10) { counter ->
            ImageSample.mock().run {
                copy(
                    metadata = metadata.copy(sample = counter),
                    stage = AnalysisStage.Analyzed,
                )
            }
        }.toMutableList()

        // Every image is analyzed and thus the diagnose should be analyzed
        diagnosis = diagnosis.copy(images = images)
        Assert.assertTrue(diagnosis.analyzed)

        // One of the images is not analyzed
        images[0] = images[0].copy(stage = AnalysisStage.NotAnalyzed)
        diagnosis = diagnosis.copy(images = images)
        // Thus the diagnosis must not be analyzed
        Assert.assertFalse(diagnosis.analyzed)
    }

    @Test
    fun diagnosisAppendImageTest() {
        var diagnosis = Diagnosis.mock()
        val images = List(10) { counter ->
            ImageSample.mock().run {
                copy(metadata = metadata.copy(sample = counter))
            }
        }

        Assert.assertEquals(
            diagnosis.samples,
            images.size
        )

        diagnosis = diagnosis.appendImage(ImageSample.mock())
        Assert.assertEquals(
            diagnosis.samples,
            images.size + 1
        )
    }
}