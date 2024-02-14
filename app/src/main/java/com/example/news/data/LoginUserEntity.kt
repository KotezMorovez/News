package com.example.news.data

import com.google.firebase.firestore.DocumentSnapshot

class LoginUserEntity(
   val id: String,
   val name: String,
   val email: String,
) {

   companion object{
      fun fromDocument(userDocument: DocumentSnapshot): LoginUserEntity {
         TODO("Not yet implemented")
      }
   }

   fun toDocument(){}
}