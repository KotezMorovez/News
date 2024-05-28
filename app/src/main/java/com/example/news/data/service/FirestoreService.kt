package com.example.news.data.service

import com.example.news.common.ui.GlobalConstants
import com.example.news.data.mapper.toDocument
import com.example.news.data.mapper.toFavouriteEntityList
import com.example.news.data.mapper.toProfileEntity
import com.example.news.data.model.FavouriteEntity
import com.example.news.data.model.ProfileEntity
import com.example.news.data.model.UserRegisterEntity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

interface FirebaseService {
    suspend fun updateUserData(personalInfo: ProfileEntity): Result<Unit>
    suspend fun deleteAccount(id: String): Result<Unit>
    suspend fun getProfileById(userId: String): Result<ProfileEntity>
    suspend fun registerUser(user: UserRegisterEntity, id: String): Result<Unit>
    suspend fun getCurrentUser(id: String?): Result<ProfileEntity?>
    suspend fun getCurrentUserFavourites(id: String?): Result<List<FavouriteEntity>>
    suspend fun addUserFavourite(item: FavouriteEntity, id: String): Result<Unit>
    suspend fun removeUserFavourite(item: FavouriteEntity, id: String): Result<Unit>
}

class FirestoreService @Inject constructor() : FirebaseService {
    private val database = Firebase.firestore
    private val collection = database.collection(GlobalConstants.USERS_COLLECTION)

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

    override suspend fun addUserFavourite(item: FavouriteEntity, id: String): Result<Unit> {
        return suspendCoroutine { continuation ->
            collection
                .document(id)
                .collection(GlobalConstants.FAVOURITES_COLLECTION)
                .document(item.id)
                .set(item.toDocument())
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(Result.success(Unit)))
                }
                .addOnFailureListener {
                    continuation.resumeWith(Result.success(Result.failure(it)))
                }
        }
    }

    override suspend fun removeUserFavourite(item: FavouriteEntity, id: String): Result<Unit> {
        return suspendCoroutine { continuation ->
            collection
                .document(id)
                .collection(GlobalConstants.FAVOURITES_COLLECTION)
                .document(item.id)
                .delete()
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
                sources = user.sources,
            )

            collection
                .document(id)
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
                collection
                    .document(id)
                    .get()
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

    override suspend fun getCurrentUserFavourites(id: String?): Result<List<FavouriteEntity>> {
        return suspendCoroutine { continuation ->
            if (id != null) {
                collection
                    .document(id)
                    .collection(GlobalConstants.FAVOURITES_COLLECTION)
                    .get()
                    .addOnSuccessListener {
                        continuation.resumeWith(Result.success(Result.success(it.toFavouriteEntityList())))
                    }
                    .addOnFailureListener {
                        continuation.resumeWith(Result.success(Result.failure(it)))
                    }
            }
        }
    }
}

/**
 * Как вложить два запроса в один результат
 *
 * override suspend fun getCurrentUser(id: String?): Result<ProfileEntity?> {
 *    return if (id != null) {
 *        suspendCoroutine { continuation ->
 *            val userReference = collection.document(id)
 *            val favouritesListReference = collection
 *                .document(id)
 *                .collection(GlobalConstants.FAVOURITES_COLLECTION)
 *
 *            userReference
 *                .get()
 *                .addOnSuccessListener { user ->
 *                    favouritesListReference
 *                        .get()
 *                        .addOnSuccessListener {
 *                            val profile = user.toProfileEntity(it.documents)
 *                            continuation.resumeWith(
 *                                Result.success(
 *                                    Result.success(profile)
 *                                )
 *                            )
 *                        }
 *                        .addOnFailureListener {
 *
 *                        }
 *
 *
 *                }
 *                .addOnFailureListener {
 *                    continuation.resumeWith(Result.success(Result.failure(it)))
 *                }
 *        }
 *    } else {
 *        Result.success(null)
 *    }
 * }
 */