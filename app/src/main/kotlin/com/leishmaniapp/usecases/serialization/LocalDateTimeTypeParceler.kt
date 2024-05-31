package com.leishmaniapp.usecases.serialization

import android.os.Parcel
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.parcelize.Parceler

object LocalDateTimeTypeParceler : Parceler<LocalDateTime> {
    override fun create(parcel: Parcel): LocalDateTime =
        parcel.readString()?.toLocalDateTime()!!

    override fun LocalDateTime.write(parcel: Parcel, flags: Int) {
        parcel.writeString(this.toString())
    }
}