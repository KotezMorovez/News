package com.example.news.data.service

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

interface CloudStorageService {
    suspend fun uploadImage(image: ByteArray, id: String): Result<String>
}

class StorageService @Inject constructor() : CloudStorageService {
    private val storage = Firebase.storage
    private val rootReference = storage.reference

    override suspend fun uploadImage(image: ByteArray, id: String): Result<String> {
        return suspendCoroutine { continuation ->
            val url = "avatars/${id}/${UUID.randomUUID()}.jpg"

            rootReference.child("avatars/$id/").listAll()
                .addOnFailureListener {
                    continuation.resumeWith(Result.success(Result.failure(it)))
                }
                .addOnSuccessListener {
                    it.items.forEach { file ->
                        rootReference.child(file.path).delete()
                    }

                    rootReference.child(url).putBytes(image)
                        .addOnSuccessListener {
                            rootReference.child(url)
                                .downloadUrl
                                .addOnSuccessListener { uri ->
                                    continuation.resumeWith(Result.success(Result.success(uri.toString())))
                                }
                                .addOnFailureListener { exception ->
                                    continuation.resumeWith(Result.success(Result.failure(exception)))
                                }
                        }
                        .addOnFailureListener {exception ->
                            continuation.resumeWith(Result.success(Result.failure(exception)))
                        }
                }
        }
    }
}