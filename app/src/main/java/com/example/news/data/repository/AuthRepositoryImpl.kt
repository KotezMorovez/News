package com.example.news.data.repository

import com.example.news.data.service.AuthService
import com.example.news.data.service.FirebaseAuthService
import com.example.news.data.mapper.toDomain
import com.example.news.data.mapper.toEntity
import com.example.news.data.service.FirebaseService
import com.example.news.data.service.FirestoreService
import com.example.news.domain.model.AuthUser
import com.example.news.domain.model.auth.LoginRequest
import com.example.news.domain.model.auth.SignUpRequest
import com.example.news.domain.repository.AuthRepository
import java.lang.NullPointerException

class AuthRepositoryImpl : AuthRepository {
    companion object {
        private var instance: AuthRepositoryImpl? = null
        fun getInstance(): AuthRepositoryImpl {
            if (instance == null) {
                instance = AuthRepositoryImpl()
            }
            return instance!!
        }
    }

    private val authService: AuthService = FirebaseAuthService.getInstance()
    private val userService: FirebaseService = FirestoreService.getInstance()

    override suspend fun isUserAuthorized(): Boolean {
        return !authService.getCurrentUserId().isNullOrEmpty()
    }

    override suspend fun isUserVerified(): Boolean {
        return authService.getUserVerificationStatus()
    }

    override suspend fun getUserId(): String? {
        return authService.getCurrentUserId()
    }

    override suspend fun sendVerificationEmail(): Result<Unit> {
        return authService.sendVerificationEmail()
    }

    override suspend fun loginUser(loginUser: LoginRequest): Result<AuthUser> {
        return authService.loginUser(loginUser.email, loginUser.password).map { it.toDomain() }
    }

    override suspend fun registerUser(user: SignUpRequest): Result<Unit> {
        val id = authService.registerUser(user.toEntity())
        return if (id.isSuccess) {
            userService.registerUser(user.toEntity(), id.getOrNull().toString())
        } else {
            Result.failure(IllegalStateException("Register user was failed"))
        }
    }

    override suspend fun logoutUser(): Result<Unit> {
        return authService.logoutUser()
    }

    override suspend fun deleteAccount(): Result<Unit> {
        val id = getUserId()
        if (id != null) {
            val result = userService.deleteAccount(id)
            if (result.isSuccess) {
                authService.deleteAccount()
                return Result.success(Unit)
            } else return Result.failure(result.exceptionOrNull()!!)
        } else return Result.failure(NullPointerException())
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return authService.resetPassword(email)
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit> {
        return authService.changePassword(oldPassword, newPassword)
    }
}