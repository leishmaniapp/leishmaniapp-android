package com.leishmaniapp

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
import com.leishmaniapp.entities.disease.Disease
import com.leishmaniapp.entities.disease.LeishmaniasisGiemsaDisease
import com.leishmaniapp.entities.mock.MockGenerator
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.Test
import org.mindrot.jbcrypt.BCrypt

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
    fun passwordAutomaticBcryptHash() {
        val specialist = Specialist(
            "John Doe",
            Username("john_doe"),
            Password("password")
        )

        Assert.assertNotEquals(specialist.password!!.value, "password")
        Assert.assertTrue(BCrypt.checkpw("password", specialist.password!!.value))
    }

    @Test
    fun diagnosisJsonSerializationIncludesAllEntities() {
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
            images = mapOf(
                1 to Image(
                    sample = 1,
                    size = 2250,
                    processed = true,
                    elements = setOf(
                        SpecialistDiagnosticElement(
                            LeishmaniasisGiemsaDisease.elements.first(),
                            10
                        ),
                        ModelDiagnosticElement(
                            LeishmaniasisGiemsaDisease.elements.first(),
                            LeishmaniasisGiemsaDisease.models.first(),
                            coordinates = setOf(
                                Coordinates(10, 10)
                            )
                        )
                    )
                )
            )
        )

        val expectedJson = """
            {
              "id": "${diagnosis.id}",
              "specialistResult": ${diagnosis.specialistResult},
              "modelResult": ${diagnosis.modelResult},
              "date": "${diagnosis.date}",
              "remarks": "${diagnosis.remarks}",
              "specialist": {
                "name": "${diagnosis.specialist.name}",
                "username": "${diagnosis.specialist.username.value}"
              },
              "patient": "${diagnosis.patient.hash}",
              "disease": "${diagnosis.disease.id}",
              "images": {
                "1": {
                  "sample": ${diagnosis.images[1]!!.sample},
                  "date": "${diagnosis.images[1]!!.date}",
                  "size": ${diagnosis.images[1]!!.size},
                  "processed": ${diagnosis.images[1]!!.processed},
                  "elements": [
                    {
                      "type": "${
            when (diagnosis.images[1]!!.elements.elementAt(0)) {
                is SpecialistDiagnosticElement -> "specialist"
                is ModelDiagnosticElement -> "model"
            }
        }",
                      "name": "${
            diagnosis.images[1]!!.elements.filterIsInstance<SpecialistDiagnosticElement>()
                .first().name.value
        }",
                      "amount": ${
            diagnosis.images[1]!!.elements.filterIsInstance<SpecialistDiagnosticElement>()
                .first().amount
        }
                   },
                    {
                      "type": "${
            when (diagnosis.images[1]!!.elements.elementAt(1)) {
                is SpecialistDiagnosticElement -> "specialist"
                is ModelDiagnosticElement -> "model"
            }
        }",
                      "name": "${
            diagnosis.images[1]!!.elements.filterIsInstance<ModelDiagnosticElement>()
                .first().name.value
        }",
                      "model": "${
            diagnosis.images[1]!!.elements.filterIsInstance<ModelDiagnosticElement>()
                .first().model.value
        }",
                      "coordinates": [
                        {
                          "x": ${
            diagnosis.images[1]!!.elements.filterIsInstance<ModelDiagnosticElement>()
                .first().coordinates.first().x
        },
                          "y": ${
            diagnosis.images[1]!!.elements.filterIsInstance<ModelDiagnosticElement>()
                .first().coordinates.first().y
        }
                        }
                      ]
                    }
                  ]
                }
              }
            }
        """.replace("\n", "").replace("\t", "").replace(" ", "")

        Assert.assertEquals(expectedJson, Json.encodeToString(diagnosis))
    }

}