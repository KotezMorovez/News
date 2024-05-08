package com.example.news.data.model

class ProfileEntity(
   val id: String,
   val name: String,
   val email: String,
   val imageUrl: String?,
   val language: String,
   val sources: List<String>
)