package com.example.uploadingscreen.model

import com.google.firebase.firestore.auth.User

data class LoginResponse(
    val message : String,
    val user : User?
)
