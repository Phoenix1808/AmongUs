package com.example.uploadingscreen.model

data class AvailableRoomResponse(
    val code : String,
    val host: HostInfo,
    val maxPlayers : Int,
    val currentPlayers : Int,
    val createdAt: String
)

data class HostInfo(
    val username:String
)
