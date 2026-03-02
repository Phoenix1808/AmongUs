package com.example.uploadingscreen.model


data class RoomLookupResponse(
    val code: String,
    val host: String,
    val status: String,
    val maxPlayers: Int,
    val players: List<String>,
    val createdAt: String
)
