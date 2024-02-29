package com.example.news.data.service

import android.util.Log
import com.example.news.data.model.ProfileEntity
import com.example.news.data.model.UserRegisterEntity
import com.example.news.data.mapper.toDocument
import com.example.news.data.mapper.toProfileEntity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.suspendCoroutine

interface AuthService {

    suspend fun getCurrentUserId(): String?

    suspend fun loginUser(login: String, password: String): Result<FirebaseUser>

    suspend fun logoutUser(): Result<Unit>

    suspend fun deleteAccount()

    suspend fun registerUser(user: UserRegisterEntity): Result<String>

    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit>
}

class FirebaseAuthService() : AuthService {
    private val auth = FirebaseAuth.getInstance()

    companion object {
        private var instance: FirebaseAuthService? = null
        fun getInstance(): FirebaseAuthService {
            if (instance == null) {
                instance = FirebaseAuthService()
            }
            return instance!!
        }
    }

    override suspend fun getCurrentUserId(): String? {
        return if (auth.currentUser != null) {
            auth.currentUser!!.uid
        } else {
            null
        }
    }

    override suspend fun loginUser(login: String, password: String): Result<FirebaseUser> {
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(login, password)
                .addOnSuccessListener {
                    if (it.user != null) {
                        continuation.resumeWith(Result.success(Result.success(it.user!!)))
                    } else {
                        continuation.resumeWith(
                            Result.success(Result.failure(FirebaseAuthInvalidUserException("", "")))
                        )
                    }
                }
                .addOnFailureListener {
                    continuation.resumeWith(Result.success(Result.failure(it)))
                }
        }
    }

    override suspend fun logoutUser(): Result<Unit> {
        auth.signOut()
        return Result.success(Unit)
    }

    override suspend fun deleteAccount() {
        auth.currentUser!!.delete()
    }

    override suspend fun registerUser(user: UserRegisterEntity): Result<String> {
        return suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnSuccessListener { result ->
                    if (result.user == null) {
                        continuation.resumeWith(
                            Result.success(
                                Result.failure(
                                    IllegalStateException("User must be returned from authorization")
                                )
                            )
                        )
                    } else {
                        continuation.resumeWith(
                            Result.success(Result.success(result.user!!.uid))
                        )
                    }
                }
                .addOnFailureListener {
                    continuation.resumeWith(Result.success(Result.failure(it)))
                }
        }
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit> {
        return suspendCoroutine { continuation ->
            var user = auth.currentUser!!
            user.reauthenticate(EmailAuthProvider.getCredential(user.email!!, oldPassword))
                .addOnSuccessListener {
                    user = auth.currentUser!!
                    user.updatePassword(newPassword)
                        .addOnSuccessListener {
                            continuation.resumeWith(Result.success(Result.success(Unit)))
                        }
                        .addOnFailureListener {
                            continuation.resumeWith(Result.success(Result.failure(it)))
                        }
                }
                .addOnFailureListener {
                    continuation.resumeWith(Result.success(Result.failure(it)))
                }
        }
    }
}