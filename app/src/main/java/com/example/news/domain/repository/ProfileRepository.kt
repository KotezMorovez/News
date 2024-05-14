package com.example.news.domain.repository

import android.graphics.Bitmap
import com.example.news.domain.model.home.FavouriteItem
import com.example.news.domain.model.home.response.Favourite
import com.example.news.domain.model.profile.Profile

interface ProfileRepository {
    suspend fun saveImage(image: Bitmap, id: String): Result<String>
    suspend fun getProfile(): Result<Profile?>
    suspend fun getProfileById(userId: String): Result<Profile>
    suspend fun updateProfileData(profile: Profile): Result<Unit>
    suspend fun addFavourite(item: Favourite, userId: String): Result<Unit>
    suspend fun removeFavourite(item: Favourite, userId: String): Result<Unit>
    suspend fun getFavourites(): Result<List<Favourite>>
}