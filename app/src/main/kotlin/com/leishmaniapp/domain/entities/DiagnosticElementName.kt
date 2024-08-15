package com.leishmaniapp.domain.entities

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leishmaniapp.R
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.serialization.DiagnosticElementNameSerializer
import com.leishmaniapp.domain.types.Identificator
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * [DiagnosticElement] unique ID with a display name
 */
@Parcelize
@Serializable(with = DiagnosticElementNameSerializer::class)
data class DiagnosticElementName(
    /**
     * Unique id for the diagnostic element, the format must be <diasease>:<element>
     * LAM modules are allowed to use just <element> as ID
     *
     * **Important:** Names must not be contained one into another ej: 'parasites' and 'parasites_b' are
     * not valid as 'parasites' is contained within 'parasites_b'
     */
    val id: Identificator,

    /**
     * Android resource for showing the element name
     */
    @Transient @StringRes val displayNameResource: Int = R.string.unknown_element,

    ) : Parcelable {

    val displayName: String
        @Composable get() = stringResource(id = displayNameResource)

    companion object {
        /**
         * Get one of the registered [DiagnosticElementName] given an identificator string
         * Both _disease:item_ or _item_ should work
         */
        fun diagnosticElementNameById(id: String): DiagnosticElementName? =
            Disease.diseases().flatMap { it.elements }.firstOrNull { it.id.contains(id) }
    }

    override fun toString(): String = id

    override fun equals(other: Any?): Boolean =
        (other is DiagnosticElementName) && (other.id == id)

    override fun hashCode(): Int = id.hashCode()
}