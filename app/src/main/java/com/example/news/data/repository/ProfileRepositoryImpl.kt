package com.example.news.data.repository

import android.graphics.Bitmap
import com.example.news.data.mapper.toDomain
import com.example.news.data.mapper.toEntity
import com.example.news.data.service.AuthService
import com.example.news.data.service.CloudStorageService
import com.example.news.data.service.FirebaseAuthService
import com.example.news.data.service.FirebaseService
import com.example.news.data.service.FirestoreService
import com.example.news.data.service.StorageService
import com.example.news.domain.model.profile.Profile
import com.example.news.domain.repository.ProfileRepository
import com.example.news.common.ui.BitmapUtils
import com.example.news.domain.model.home.response.Favourite
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val userService: FirebaseService,
    private val authService: AuthService,
    private val storageService: CloudStorageService
) : ProfileRepository {

    override suspend fun saveImage(image: Bitmap, id: String): Result<String> {
        val byteArray = BitmapUtils.convertBitmapToByteArray(image)
        return storageService.uploadImage(byteArray, id)
    }

    override suspend fun getProfile(): Result<Profile?> {
        val userId = authService.getCurrentUserId()
        val result = userService.getCurrentUser(userId)
        return result.map { it?.toDomain() }
    }

    override suspend fun getFavourites(): Result<List<Favourite>> {
        val userId = authService.getCurrentUserId() ?: return Result.success(listOf())
        return userService.getCurrentUserFavourites(userId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getProfileById(userId: String): Result<Profile> {
        return userService.getProfileById(userId).map { it.toDomain() }
    }

    override suspend fun updateProfileData(profile: Profile): Result<Unit> {
        return userService.updateUserData(profile.toEntity())
    }

    override suspend fun addFavourite(item: Favourite, userId: String): Result<Unit> {
        return userService.addUserFavourite(item.toEntity(), userId)
    }

    override suspend fun removeFavourite(item: Favourite, userId: String): Result<Unit> {
        return userService.removeUserFavourite(item.toEntity(), userId)
    }
}