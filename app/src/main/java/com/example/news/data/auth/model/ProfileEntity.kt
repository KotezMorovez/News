package com.example.news.data.auth.model

import com.google.firebase.firestore.DocumentSnapshot

class ProfileEntity(
   val id: String,
   val name: String,
   val email: String,
   val imageUrl: String?
)