package com.example.news.domain.model.auth.repository

import com.example.news.domain.model.User
import com.example.news.domain.model.auth.LoginRequest
import com.example.news.domain.model.auth.Profile
import com.example.news.domain.model.auth.SignUpRequest

interface AuthRepository {

    suspend fun isUserAuthorized(): Boolean
    suspend fun getUserId(): String?
    suspend fun getUser(): Result<Profile?>
    suspend fun getUserById(userId: String): Result<Profile>
    suspend fun loginUser(loginUser: LoginRequest): Result<User>
    suspend fun registerUser(user: SignUpRequest): Result<Unit>
    suspend fun logoutUser()
    suspend fun deleteAccount(): Result<Unit>
    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit>
}