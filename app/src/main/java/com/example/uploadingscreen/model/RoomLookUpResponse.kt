package com.example.uploadingscreen.model


data class RoomLookupResponse(
    val _id: String,
    val code: String,
    val host: String,
    val players: List<Player>,
    val state: String,
    val maxPlayers: Int,
    val createdAt: String,
    val updatedAt: String,
    val __v:Int
)
