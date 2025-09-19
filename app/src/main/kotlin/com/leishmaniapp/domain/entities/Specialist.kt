package com.leishmaniapp.domain.entities

import android.os.Parcelable
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.types.Email
import kotlinx.parcelize.Parcelize

/**
 * Specialist representation
 * Algebraic product type with Username and Password which are inlined
 */
@Parcelize
data class Specialist(

    val name: String,
    val email: Email,
    val diseases: Set<Disease> = setOf()

) : Parcelable {

    companion object;

    /**
     * [Specialist] with only the [Specialist.email] and [Specialist.name] fields
     */
    @Parcelize
    data class Record(
        val name: String,
        val email: Email,
    ) : Parcelable {

        companion object;

    }
}