package com.example.news.data.auth.model

import com.example.news.domain.model.User
import com.example.news.domain.model.auth.Profile
import com.example.news.domain.model.auth.SignUpRequest
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot

fun ProfileEntity.toDomain(): Profile {
    return Profile(
        id = this.id,
        email = this.email,
        name = this.name,
        imageUrl = this.imageUrl
    )
}

fun DocumentSnapshot.toProfileEntity(): ProfileEntity {
    return ProfileEntity(
        id = this.id,
        email = this["email"] as String,
        name = this["name"] as String,
        imageUrl = this["image_url"] as String
    )
}
fun ProfileEntity.toDocument(): Map <String, Any>{
    return mapOf(
        "email" to this.email,
        "name" to this.name,
        "image_url" to (this.imageUrl ?: "")
    )
}

fun SignUpRequest.toEntity(): UserRegisterEntity {
    return UserRegisterEntity(
        email = this.email,
        password = this.password,
        name = this.name,
    )
}

fun FirebaseUser.toDomain(): User {
    return User(
        id = this.uid,
        email = this.email ?: ""
    )
}