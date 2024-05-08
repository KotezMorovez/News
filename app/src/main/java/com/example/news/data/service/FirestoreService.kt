package com.example.news.data.service

import com.example.news.data.mapper.toDocument
import com.example.news.data.mapper.toProfileEntity
import com.example.news.data.model.ProfileEntity
import com.example.news.data.model.UserRegisterEntity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.suspendCoroutine

interface FirebaseService {
    suspend fun updateUserData(personalInfo: ProfileEntity): Result<Unit>
    suspend fun deleteAccount(id: String): Result<Unit>
    suspend fun getProfileById(userId: String): Result<ProfileEntity>
    suspend fun registerUser(user: UserRegisterEntity, id: String): Result<Unit>
    suspend fun getCurrentUser(id: String?): Result<ProfileEntity?>
}

class FirestoreService : FirebaseService {
    private val database = Firebase.firestore
    private val collection = database.collection("profiles")

    companion object {
        private var instance: FirestoreService? = null
        fun getInstance(): FirestoreService {
            if (instance == null) {
                instance = FirestoreService()
            }
            return instance!!
        }
    }

    override suspend fun updateUserData(personalInfo: ProfileEntity): Result<Unit> {
        return suspendCoroutine { continuation ->
            collection
                .document(personalInfo.id)
                .set(personalInfo.toDocument())
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(Result.success(Unit)))
                }
                .addOnFailureListener {
                    continuation.resumeWith(Result.success(Result.failure(it)))
                }
        }
    }

    override suspend fun deleteAccount(id: String): Result<Unit> {
        return suspendCoroutine { continuation ->
            collection
                .document(id)
                .delete()
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(Result.success(Unit)))
                }
                .addOnFailureListener {
                    continuation.resumeWith(Result.success(Result.failure(it)))
                }
        }
    }

    override suspend fun getProfileById(userId: String): Result<ProfileEntity> {
        return suspendCoroutine { continuation ->
            val docRef = collection.document(userId).get()
            docRef
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
    }

    override suspend fun registerUser(user: UserRegisterEntity, id: String): Result<Unit> {
        return suspendCoroutine { continuation ->
            val userEntity = ProfileEntity(
                id = id,
                email = user.email,
                name = user.name,
                imageUrl = "",
                language = user.language,
                sources = user.sources
            )

            collection
                .document(userEntity.id)
                .set(userEntity.toDocument())
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(Result.success(Unit)))
                }
                .addOnFailureListener {
                    continuation.resumeWith(Result.success(Result.failure(it)))
                }


        }
    }

    override suspend fun getCurrentUser(id: String?): Result<ProfileEntity?> {
        return if (id != null) {
            suspendCoroutine { continuation ->
                val user = collection.document(id).get()
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
}
