package com.example.news.data.mapper

import com.example.news.common.ui.StringUtils
import com.example.news.data.model.FavouriteEntity
import com.example.news.data.model.ProfileEntity
import com.example.news.domain.model.home.response.Favourite
import com.example.news.domain.model.profile.Profile
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

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

fun DocumentSnapshot.toFavouriteEntity(): FavouriteEntity {
    return FavouriteEntity(
        id = this.id,
        title = this["title"] as String,
        url = this["url"] as String,
        image = this["image"] as String
    )
}

fun QuerySnapshot.toFavouriteEntityList(): List<FavouriteEntity> {
    return this.documents.map { it.toFavouriteEntity() }
}

fun FavouriteEntity.toDomain(): Favourite {
    return Favourite(
        id = StringUtils.urlDecoding(this.id),
        title = this.title,
        url = this.url,
        image = this.image
    )
}

fun Favourite.toEntity(): FavouriteEntity {
    return FavouriteEntity(
        id = StringUtils.urlEncoding(this.id),
        title = this.title,
        url = this.url,
        image = this.image
    )
}

fun FavouriteEntity.toDocument(): Map<String, Any> {
    return mapOf(
        "title" to this.title,
        "url" to this.url,
        "image" to this.image
    )
}

fun ProfileEntity.toDocument(): Map<String, Any> {
    return mapOf(
        "email" to this.email,
        "name" to this.name,
        "image_url" to (this.imageUrl ?: ""),
        "language" to this.language,
        "sources" to this.sources
    )
}

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