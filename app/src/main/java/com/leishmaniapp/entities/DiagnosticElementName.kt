package com.leishmaniapp.entities

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leishmaniapp.R
import com.leishmaniapp.usecases.serialization.DiagnosticElementNameSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable(with = DiagnosticElementNameSerializer::class)
class DiagnosticElementName(
    val value: String,
    @StringRes private val displayNameResource: Int = R.string.unknown_element
) : Parcelable {
    val displayName: String
        @Composable get() = stringResource(id = displayNameResource)

    override fun toString(): String = value

    override fun equals(other: Any?): Boolean =
        (other is DiagnosticElementName) && (other.value == value)

    override fun hashCode(): Int = value.hashCode()
}