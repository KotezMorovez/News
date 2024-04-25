package com.example.news.domain.repository

import com.example.news.domain.model.AuthUser
import com.example.news.domain.model.auth.LoginRequest
import com.example.news.domain.model.profile.Profile
import com.example.news.domain.model.auth.SignUpRequest

interface AuthRepository {
    suspend fun isUserAuthorized(): Boolean
    suspend fun isUserVerified(): Boolean
    suspend fun sendVerificationEmail(): Result<Unit>
    suspend fun getUserId(): String?
    suspend fun loginUser(loginUser: LoginRequest): Result<AuthUser>
    suspend fun registerUser(user: SignUpRequest): Result<Unit>
    suspend fun logoutUser(): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit>
}