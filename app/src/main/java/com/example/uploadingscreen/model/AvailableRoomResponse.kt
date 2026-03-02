package com.example.uploadingscreen.model

data class AvailableRoomResponse(
    val _id: String,
    val code : String,
    val host: HostInfo,
    val players: List<String>,
    val maxPlayers : Int,
    val createdAt: String
)

data class HostInfo(
    val _id: String,
    val username:String
)
