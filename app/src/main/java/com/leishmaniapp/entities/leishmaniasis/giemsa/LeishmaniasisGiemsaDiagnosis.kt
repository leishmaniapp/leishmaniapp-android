package com.leishmaniapp.entities.leishmaniasis.giemsa

import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Disease
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.Patient
import com.leishmaniapp.entities.Specialist
import kotlinx.datetime.LocalDateTime

class LeishmaniasisGiemsaDiagnosis(
    override val result: Boolean,
    override val date: LocalDateTime,
    override val remarks: String,
    override val specialist: Specialist,
    override val patientDiagnosed: Patient,
    override val diagnosticDisease: Disease,
    override val diagnosticImages: MutableSet<Image>,
    override val resultsComputed: Boolean = false,
) : Diagnosis(
    result,
    date,
    remarks,
    specialist,
    patientDiagnosed,
    diagnosticDisease,
    diagnosticImages,
    resultsComputed
) {
    val specialistMacrophagesAmount: Int = 0
    val specialistParasitesAmount: Int = 0
    val modelMacrophagesAmount: Int = 0
    val modelParasitesAmount: Int = 0
    override fun computeResults() {
        TODO("Not yet implemented")
    }
}