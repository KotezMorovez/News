package com.example.news.data.service

import com.example.news.data.model.UserRegisterEntity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

interface AuthService {
    suspend fun getUserVerificationStatus(): Boolean
    suspend fun getCurrentUserId(): String?
    suspend fun loginUser(login: String, password: String): Result<FirebaseUser>
    suspend fun logoutUser(): Result<Unit>
    suspend fun sendVerificationEmail(): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
    suspend fun registerUser(user: UserRegisterEntity): Result<String>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit>
}

class FirebaseAuthService @Inject constructor() : AuthService {
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

    override suspend fun getUserVerificationStatus(): Boolean {
        return auth.currentUser?.isEmailVerified ?: false
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

    override suspend fun deleteAccount(): Result<Unit> {
        return suspendCoroutine {continuation ->
            auth.currentUser!!.delete()
                .addOnSuccessListener {
                    continuation.resumeWith(
                        Result.success(
                            Result.success(Unit)
                        )
                    )
                }
                .addOnFailureListener {
                    continuation.resumeWith(
                        Result.success(
                            Result.failure(
                                IllegalStateException("Can't delete this account")
                            )
                        )
                    )
                }
        }
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
                        sendVerificationEmail(
                            onSuccess = {
                                continuation.resumeWith(
                                    Result.success(
                                        Result.success(
                                            result.user!!.uid
                                        )
                                    )
                                )
                            },
                            onFailure = { continuation.resumeWith(Result.success(Result.failure(it))) }
                        )
                    }
                }
                .addOnFailureListener {
                    continuation.resumeWith(Result.success(Result.failure(it)))
                }
        }
    }

    override suspend fun sendVerificationEmail(): Result<Unit> {
        return suspendCoroutine { continuation ->
            sendVerificationEmail(
                onSuccess = { continuation.resumeWith(Result.success(Result.success(Unit))) },
                onFailure = { continuation.resumeWith(Result.success(Result.failure(it))) }
            )
        }
    }

    private fun sendVerificationEmail(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (auth.currentUser == null) {
            onFailure(IllegalStateException("User must be returned from authorization"))
        } else {
            auth.currentUser!!.sendEmailVerification()
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { onFailure(it) }
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return suspendCoroutine { continuation ->
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(Result.success(Unit)))
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