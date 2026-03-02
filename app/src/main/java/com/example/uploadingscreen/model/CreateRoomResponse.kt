package com.example.uploadingscreen.model

data class CreateRoomResponse(

    val code: String,
    val host: String,
    val players: List<String>,
    val maxPlayers: Int,
    val state: String,
    val _id: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

