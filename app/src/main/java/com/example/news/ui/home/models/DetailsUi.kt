package com.example.news.ui.home.models

import android.os.Parcel
import android.os.Parcelable

data class DetailsUi(
    val id: String,
    val header: String,
    val body: String,
    val url: String,
    val imagesUriList: List<String>?,
    val date: String,
    var isFavorite: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList(),
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(header)
        parcel.writeString(body)
        parcel.writeString(url)
        parcel.writeStringList(imagesUriList)
        parcel.writeString(date)
        parcel.writeByte(if (isFavorite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DetailsUi> {
        override fun createFromParcel(parcel: Parcel): DetailsUi {
            return DetailsUi(parcel)
        }

        override fun newArray(size: Int): Array<DetailsUi?> {
            return arrayOfNulls(size)
        }
    }
}