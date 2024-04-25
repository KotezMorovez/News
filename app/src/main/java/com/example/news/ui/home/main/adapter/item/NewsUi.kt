package com.example.news.ui.home.main.adapter.item

import android.os.Parcel
import android.os.Parcelable

data class NewsUi(
    val id: String,
    val header: String,
    val body: String,
    val imagesUriList: List<String>?,
    val date: String,
    val isFavorite: Boolean
)