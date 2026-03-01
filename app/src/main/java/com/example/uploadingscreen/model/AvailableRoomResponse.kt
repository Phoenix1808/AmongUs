package com.example.uploadingscreen.model

data class AvailableRoomResponse(
    val code : String,
    val host: Host,
    val players : List<String>,
    val maxPlayers : Int,
    val CreatedAt: String
)
