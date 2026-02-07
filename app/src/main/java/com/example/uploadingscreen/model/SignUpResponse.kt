package com.example.uploadingscreen.model

import com.google.firebase.firestore.auth.User

data class SignUpResponse (
    val message : String,
    val user : User?
)