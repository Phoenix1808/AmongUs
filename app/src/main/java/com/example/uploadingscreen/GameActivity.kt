package com.example.uploadingscreen

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

//removed the socket.on("game:role") bcoz this event was getting fired just after the game:started evvent so it was done in LobbyActivity part
//Now this screen will show the room code with role assigned form the server and then the status of game will be "Game Started"
class GameActivity : AppCompatActivity() {

    private var roomCode: String? = null
    private var role: String? = null

    private lateinit var tvRoomCode: TextView
    private lateinit var tvRole: TextView
    private lateinit var tvStatus : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)

        roomCode = intent.getStringExtra("roomCode")
        role = intent.getStringExtra("role")

        tvRole=findViewById(R.id.tvRole)
        tvRoomCode= findViewById(R.id.tvRoomCode)
        tvStatus = findViewById(R.id.tvStatus)

        tvRoomCode.text = "Room Code : $roomCode"
        tvStatus.text = "Waiting For Role.."

        if(role!=null){
            tvRole.text = "Role: $role"
            tvStatus.text = "Game Started"
            if(role=="imposter"){
                tvRole.setTextColor(getColor(android.R.color.holo_red_dark))
            } else{
                tvRole.setTextColor(getColor(android.R.color.holo_green_dark))
            }
        } else{
            tvStatus.text = "Role Not Assigned"
        }
    }
}