package com.example.news.data.mapper

import com.example.news.data.model.ProfileEntity
import com.example.news.domain.model.profile.Profile
import com.google.firebase.firestore.DocumentSnapshot

fun ProfileEntity.toDomain(): Profile {
    return Profile(
        id = this.id,
        email = this.email,
        name = this.name,
        imageUrl = this.imageUrl,
        language = this.language,
        sources = this.sources
    )
}

fun DocumentSnapshot.toProfileEntity(): ProfileEntity {
    return ProfileEntity(
        id = this.id,
        email = this["email"] as String,
        name = this["name"] as String,
        imageUrl = this["image_url"] as String,
        language = this["language"] as String,
        sources = this["sources"] as List<String>
    )
}
fun ProfileEntity.toDocument(): Map <String, Any>{
    return mapOf(
        "email" to this.email,
        "name" to this.name,
        "image_url" to (this.imageUrl ?: ""),
        "language" to this.language,
        "sources" to this.sources
    )
}

fun Profile.toEntity(): ProfileEntity {
    return ProfileEntity(
        id = this.id,
        name = this.name,
        email = this.email,
        imageUrl = this.imageUrl,
        language = this.language,
        sources = this.sources
    )
}