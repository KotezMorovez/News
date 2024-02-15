package com.example.news.data.auth

import com.example.news.data.auth.model.toDomain
import com.example.news.data.auth.model.toEntity
import com.example.news.domain.model.User
import com.example.news.domain.model.auth.LoginRequest
import com.example.news.domain.model.auth.Profile
import com.example.news.domain.model.auth.SignUpRequest
import com.example.news.domain.model.auth.repository.AuthRepository

class AuthRepositoryImpl: AuthRepository {
    companion object{
        private var instance: AuthRepositoryImpl? = null
        fun getInstance() : AuthRepositoryImpl{
            if (instance == null){
                instance = AuthRepositoryImpl()
            }
                return instance!!
        }
    }

    private val authService: AuthService = FirebaseAuthService.getInstance()
    override suspend fun isUserAuthorized(): Boolean {
        return !authService.getCurrentUserId().isNullOrEmpty()
    }

    override suspend fun getUserId(): String? {
        return authService.getCurrentUserId()
    }

    override suspend fun getUser(): Result<Profile?> {
        return authService.getCurrentUser().map { it?.toDomain() }
    }

    override suspend fun getUserById(userId: String): Result<Profile> {
        return authService.getProfileById(userId).map { it.toDomain() }
    }

    override suspend fun loginUser(loginUser: LoginRequest): Result<User> {
        return authService.loginUser(loginUser.email, loginUser.password).map { it.toDomain()}
    }

    override suspend fun registerUser(user: SignUpRequest): Result<Unit> {
        return authService.registerUser(user.toEntity())
    }

    override suspend fun logoutUser() {
        return authService.logoutUser()
    }


    override suspend fun deleteAccount(): Result<Unit> {
        return authService.deleteAccount()
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit> {
        return authService.changePassword(oldPassword, newPassword)
    }


}