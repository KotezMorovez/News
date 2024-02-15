package com.example.news.data.auth

import com.example.news.data.auth.model.ProfileEntity
import com.example.news.data.auth.model.UserRegisterEntity
import com.example.news.data.auth.model.toDocument
import com.example.news.data.auth.model.toProfileEntity
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

    suspend fun logoutUser()

    suspend fun deleteAccount(): Result<Unit>

    suspend fun getCurrentUser(): Result<ProfileEntity?>

    suspend fun registerUser(user: UserRegisterEntity): Result<Unit>

    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit>

    suspend fun getProfileById(userId: String): Result<ProfileEntity>
}

class FirebaseAuthService() : AuthService {
    private val database = Firebase.firestore
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

    override suspend fun logoutUser() {
        return auth.signOut()
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return suspendCoroutine { continuation ->
            val id = auth.currentUser!!.uid
            database.collection("profiles")
                .document(id)
                .delete()
                .addOnSuccessListener {
                    auth.currentUser!!.delete()
                    continuation.resumeWith(
                        Result.success(Result.success(Unit))
                    )
                }
                .addOnFailureListener {
                    continuation.resumeWith(Result.success(Result.failure(it)))
                }
        }
    }

    override suspend fun getCurrentUser(): Result<ProfileEntity?> {
        val currentId = getCurrentUserId()
        return if (currentId != null) {
            suspendCoroutine { continuation ->
                val user = database.collection("profiles").document(currentId).get()
                user
                    .addOnSuccessListener {
                        continuation.resumeWith(
                            Result.success(
                                Result.success(it.toProfileEntity())
                            )
                        )
                    }
                    .addOnFailureListener {
                        continuation.resumeWith(Result.success(Result.failure(it)))
                    }
            }
        } else {
            Result.success(null)
        }
    }

    override suspend fun registerUser(user: UserRegisterEntity): Result<Unit> {
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
                        return@addOnSuccessListener
                    }

                    val userEntity = ProfileEntity(
                        id = result.user!!.uid,
                        email = user.email,
                        name = user.name,
                        imageUrl = ""
                    )

                    database.collection("profiles")
                        .document(userEntity.id)
                        .set(userEntity.toDocument())

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

    override suspend fun getProfileById(userId: String): Result<ProfileEntity> {
        return suspendCoroutine { continuation ->
            val docRef = database.collection("profiles").document(userId).get()
            docRef
                .addOnSuccessListener { userDocument ->
                    continuation.resumeWith(
                        Result.success(
                            Result.success(userDocument.toProfileEntity())
                        )
                    )
                }
                .addOnFailureListener {
                    continuation.resumeWith(Result.success(Result.failure(it)))
                }
        }
    }
}