package com.leishmaniapp

import com.leishmaniapp.entities.Coordinates
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.DiagnosticElement
import com.leishmaniapp.entities.DocumentType
import com.leishmaniapp.entities.IdentificationDocument
import com.leishmaniapp.entities.ImageAnalysisStatus
import com.leishmaniapp.entities.ImageProcessingResponse
import com.leishmaniapp.entities.ImageQueryResponse
import com.leishmaniapp.entities.ModelDiagnosticElement
import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Patient
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.SpecialistDiagnosticElement
import com.leishmaniapp.entities.Username
import com.leishmaniapp.entities.disease.Disease
import com.leishmaniapp.entities.disease.LeishmaniasisGiemsaDisease
import com.leishmaniapp.utils.MockGenerator
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.Test

class EntitiesSerializationTests {
    @Test
    fun patientSerializationShouldGenerateHash() {
        val patient = Patient(
            "Jane Doe",
            IdentificationDocument("1234567890"),
            DocumentType.CC
        )

        Assert.assertEquals(Json.encodeToString(patient), "\"${patient.hash}\"")
    }

    @Test
    fun diseaseSerializationShouldSerializeToId() {
        val disease = LeishmaniasisGiemsaDisease as Disease
        Assert.assertEquals(Json.encodeToString(disease), "\"${disease.id}\"")
    }

    @Test
    fun diseaseSerializationFromIdShouldReturnSubclass() {
        val disease = (LeishmaniasisGiemsaDisease) as Disease

        Assert.assertEquals(
            Json.decodeFromString<Disease>("\"${disease.id}\""),
            disease
        )
    }

    @Test
    fun specialistSerializationShouldNotSerializePassword() {
        val specialist = Specialist(
            "John Doe",
            Username("john_doe"),
            Password("password")
        )

        Assert.assertEquals(
            """{"name":"${specialist.name}","username":"${specialist.username.value}"}""",
            Json.encodeToString(specialist)
        )
    }

    @Test
    fun diagnosticElementByTypeShouldSerialize() {
        val specialistElement = MockGenerator.mockSpecialistDiagnosticElement()
        val modelElement = MockGenerator.mockModelDiagnosticElement()

        print(Json.encodeToString(modelElement))

        Assert.assertEquals(
            """
            {"name":"mock.disease:mock","amount":${specialistElement.amount}}
        """.trimIndent(), Json.encodeToString(specialistElement)
        )
    }

    @Test
    fun diagnosticElementNameByStringShouldReturnDiseaseDiagnosticElementName() {
        // Serialize
        val diagnosticElement = MockGenerator.mockModelDiagnosticElement()
        val serializedValue = Json.encodeToString<DiagnosticElement>(diagnosticElement)

        // Deserialize
        Assert.assertEquals(
            diagnosticElement.name,
            Json.decodeFromString<DiagnosticElement>(serializedValue).name
        )
    }

    @Test
    fun diagnosisJsonSerializationIncludesAllEntities() {

        val images = List(2) { iterator ->
            MockGenerator.mockImage(
                sample = iterator,
                size = 1944,
                processed = ImageAnalysisStatus.Analyzed
            ).copy(elements = setOf())
        }.toMutableList()

        images[0] = images[0].copy(
            elements = setOf(
                SpecialistDiagnosticElement(
                    name = LeishmaniasisGiemsaDisease.elements.first(),
                    amount = 10
                ),
                ModelDiagnosticElement(
                    name = LeishmaniasisGiemsaDisease.elements.first(),
                    model = LeishmaniasisGiemsaDisease.models.first(),
                    coordinates = setOf(
                        Coordinates(10, 20),
                        Coordinates(30, 40),
                    )
                ),
            )
        )

        /*Strings cannot have spaces in order to trim spaces and newlines*/
        val diagnosis = Diagnosis(
            specialistResult = true,
            modelResult = false,
            remarks = "Loremipsumdolorsitametconsecteturadipiscingelit",
            specialist = Specialist(
                "JohnDoe",
                Username("john_doe"),
                Password("password")
            ),
            patient = Patient(
                "JaneDoe",
                IdentificationDocument("1234567890"),
                DocumentType.CC
            ),
            disease = LeishmaniasisGiemsaDisease,
            images = images.associateBy { it.sample }
        )

        val expectedJson = """
            {
              "id": "${diagnosis.id}",
              "specialistResult": ${diagnosis.specialistResult},
              "modelResult": ${diagnosis.modelResult},
              "date": "${diagnosis.date}",
              "remarks": "${diagnosis.remarks}",
              "specialist": "${diagnosis.specialist.username.value}",
              "patient": "${diagnosis.patient.hash}",
              "disease": "${diagnosis.disease.id}",
              "images": {
                "0": ${Json.encodeToString(images[0])},
                "1": ${Json.encodeToString(images[1])}
              }
            }
        """.replace("\n", "").replace("\t", "").replace(" ", "")

        print(Json.encodeToString(diagnosis))
        Assert.assertEquals(expectedJson, Json.encodeToString(diagnosis))
    }

    @Test
    fun imageProcessingResponseJsonSerialization() {
        val json = """
            {
                "sample": 0,
                "analysis": {
                    "leishmaniasis.giemsa:macrophages": [
                     {
                      "x": 1067,
                      "y": 712
                     }
                    ]
                }
            }
        """.replace("\n", "").replace("\t", "").replace(" ", "")

        val model = ImageProcessingResponse(
            0, mapOf(
                DiagnosisModel("leishmaniasis.giemsa:macrophages") to listOf(
                    Coordinates(1067, 712)
                )
            )
        )

        val encodedModel = Json.encodeToString(model).trimIndent()
        val decodedModel = Json.decodeFromString<ImageProcessingResponse>(json)

        Assert.assertEquals(json, encodedModel)
        Assert.assertEquals(model, decodedModel)
    }

    @Test
    fun imageProcessingResponseJsonSerializationFromList() {
        val json = """
            [{
                "sample": 0,
                "analysis": {
                    "leishmaniasis.giemsa:macrophages": [
                     {
                      "x": 1067,
                      "y": 712
                     }
                    ]
                }
            }]
        """.replace("\n", "").replace("\t", "").replace(" ", "")

        val model = listOf(
            ImageProcessingResponse(
                0, mapOf(
                    DiagnosisModel("leishmaniasis.giemsa:macrophages") to listOf(
                        Coordinates(1067, 712)
                    )
                )
            )
        )

        val encodedModel = Json.encodeToString(model).trimIndent()
        val decodedModel = Json.decodeFromString<List<ImageProcessingResponse>>(json)

        Assert.assertEquals(json, encodedModel)
        Assert.assertEquals(model, decodedModel)
    }

    @Test
    fun imageQueryResponseJSONSerializationNoAnalysis() {
        val json = """
            {"processed": false}
        """.replace("\n", "").replace("\t", "").replace(" ", "")

        val model = ImageQueryResponse(processed = false)

        val encodedModel = Json.encodeToString(model).trimIndent()
        val decodedModel = Json.decodeFromString<ImageQueryResponse>(json)

        Assert.assertEquals(json, encodedModel)
        Assert.assertEquals(model, decodedModel)
    }

    @Test
    fun imageQueryResponseJSONSerializationWithAnalysis() {
        val json = """
            {
              "processed": true,
              "analysis": {
                "mock.disease:mock": [
                  {
                    "x": 1896,
                    "y": 253
                  },
                  {
                    "x": 1785,
                    "y": 1092
                  }
                ]
              }
            }
        """.replace("\n", "").replace("\t", "").replace(" ", "")

        val model = ImageQueryResponse(
            processed = true, mapOf(
                DiagnosisModel("mock.disease:mock") to listOf(
                    Coordinates(1896, 253),
                    Coordinates(1785, 1092),
                )
            )
        )

        val encodedModel = Json.encodeToString(model).trimIndent()
        val decodedModel = Json.decodeFromString<ImageQueryResponse>(json)

        Assert.assertEquals(json, encodedModel)
        Assert.assertEquals(model, decodedModel)
    }
}