package com.example.news.data.service

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlin.coroutines.suspendCoroutine

interface CloudStorageService {
    suspend fun uploadImage(image: ByteArray, id: String): Result<String>
}

class StorageService : CloudStorageService {
    private val storage = Firebase.storage
    private val rootReference = storage.reference

    companion object {
        private var instance: StorageService? = null
        fun getInstance(): StorageService {
            if (instance == null) {
                instance = StorageService()
            }
            return instance!!
        }
    }

    override suspend fun uploadImage(image: ByteArray, id: String): Result<String> {
        return suspendCoroutine { continuation ->
            val url = "avatars/$id.jpg"
            rootReference.child(url).putBytes(image)
                .addOnSuccessListener {
                    rootReference.child(url)
                        .downloadUrl
                        .addOnSuccessListener {
                            continuation.resumeWith(Result.success(Result.success(it.toString())))
                        }
                        .addOnFailureListener {
                            continuation.resumeWith(Result.success(Result.failure(it)))
                        }
                }
                .addOnFailureListener{
                    continuation.resumeWith(Result.success(Result.failure(it)))
                }
        }
    }
}