package com.example.news.ui.profile.edit

import com.example.news.common.GlobalConstants
import java.util.Locale

data class ProfileEditItem(
    val name:String,
    val imageURL: String?,
    val email: String,
    val language: String,
    val sources: List<String>
){
    companion object {
        fun default(): ProfileEditItem {
            return ProfileEditItem(
                "",
                "",
                "",
                Locale.getDefault().language,
                GlobalConstants.DEFAULT_SOURCES
            )
        }
    }
}