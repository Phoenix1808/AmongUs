package com.example.uploadingscreen.model

data class Room(
    val _id : String,
    val code: String,
    val host: Host,
    val players: List<String>,
    val maxPlayers: Int,
    val createdAt: String
)
