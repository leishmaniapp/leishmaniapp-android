package com.leishmaniapp.entities

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leishmaniapp.R
import com.leishmaniapp.entities.disease.Disease
import com.leishmaniapp.entities.serialization.DiagnosticElementNameSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

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