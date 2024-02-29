package com.example.news.domain.repository

import android.graphics.Bitmap
import com.example.news.domain.model.profile.Profile

interface ProfileRepository {
    suspend fun saveImage(image: Bitmap, id: String): Result<String>
    suspend fun getProfile(): Result<Profile?>
    suspend fun getProfileById(userId: String): Result<Profile>
    suspend fun updateProfileData(profile: Profile): Result<Unit>
}