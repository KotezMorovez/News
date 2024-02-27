package com.example.news.data.mapper

import com.example.news.data.model.UserRegisterEntity
import com.example.news.domain.model.AuthUser
import com.example.news.domain.model.auth.SignUpRequest
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toDomain(): AuthUser {
    return AuthUser(
        id = this.uid,
        email = this.email ?: ""
    )
}

fun SignUpRequest.toEntity(): UserRegisterEntity {
    return UserRegisterEntity(
        email = this.email,
        password = this.password,
        name = this.name,
    )
}