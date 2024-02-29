package com.example.news.ui.profile.edit

data class ProfileEditItem(
    val name:String,
    val imageURL: String?,
    val email: String
){
    companion object {
        fun default(): ProfileEditItem {
            return ProfileEditItem(
                "",
                "",
                ""
            )
        }
    }
}