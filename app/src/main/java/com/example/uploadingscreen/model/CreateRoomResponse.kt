package com.example.uploadingscreen.model

data class CreateRoomResponse(
    val room : CreatedRoom
)

data class CreatedRoom(
    val code:String,
    val host:String,
    val maxPlayers : Int,
    val status: String
)
