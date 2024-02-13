package com.example.news.data

import com.google.firebase.firestore.DocumentSnapshot

class AuthUserEntity(
   val id: String,
   val name: String,
   val email: String,
) {

   companion object{
      fun fromDocument(userDocument: DocumentSnapshot): AuthUserEntity {
         TODO("Not yet implemented")
      }
   }

   fun toDocument(){}
}