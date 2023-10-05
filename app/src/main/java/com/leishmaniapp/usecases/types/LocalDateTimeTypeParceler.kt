package com.leishmaniapp.usecases.types

import android.os.Parcel
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.parcelize.Parceler

object LocalDateTimeTypeParceler : Parceler<LocalDateTime> {
    override fun create(parcel: Parcel): LocalDateTime {
        val date = parcel.readString()
        return date?.toLocalDateTime()
            ?: LocalDateTime(0, 0, 0, 0, 0)
    }

    override fun LocalDateTime.write(parcel: Parcel, flags: Int) {
        parcel.writeString(this.toString())
    }
}