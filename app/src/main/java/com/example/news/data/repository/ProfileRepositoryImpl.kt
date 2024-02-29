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
import com.example.news.common.BitmapUtils

class ProfileRepositoryImpl : ProfileRepository {
    private val userService: FirebaseService = FirestoreService.getInstance()
    private val authService: AuthService = FirebaseAuthService.getInstance()
    private val storageService: CloudStorageService = StorageService.getInstance()

    companion object{
        private var instance: ProfileRepositoryImpl? = null
        fun getInstance() : ProfileRepositoryImpl {
            if (instance == null){
                instance = ProfileRepositoryImpl()
            }
            return instance!!
        }
    }

    override suspend fun saveImage(image: Bitmap, id: String): Result<String> {
        val byteArray = BitmapUtils.convertBitmapToByteArray(image)
        return storageService.uploadImage(byteArray, id)
    }

    override suspend fun getProfile(): Result<Profile?> {
        val userId = authService.getCurrentUserId()
        val result = userService.getCurrentUser(userId)
        return result.map { it?.toDomain() }
    }

    override suspend fun getProfileById(userId: String): Result<Profile> {
        return userService.getProfileById(userId).map { it.toDomain() }
    }

    override suspend fun updateProfileData(profile: Profile): Result<Unit> {
        return userService.updateUserData(profile.toEntity())
    }
}