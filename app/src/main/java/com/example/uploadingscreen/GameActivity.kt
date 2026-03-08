package com.example.uploadingscreen

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.uploadingscreen.databinding.ActivityGameBinding

// removed the socket.on("game:role") because it fires right after game:started
// role is now passed from LobbyActivity

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    private var roomCode: String? = null
    private var role: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roomCode = intent.getStringExtra("roomCode")
        role = intent.getStringExtra("role")

        binding.tvRoomCode.text = "Room Code : $roomCode"
        binding.tvStatus.text = "Waiting For Role.."

        if (role != null) {

            binding.tvRole.text = "Role: $role"
            binding.tvStatus.text = "Game Started"

            if (role == "imposter") {
                binding.tvRole.setTextColor(getColor(android.R.color.holo_red_dark))
            } else {
                binding.tvRole.setTextColor(getColor(android.R.color.holo_green_dark))
            }

        } else {
            binding.tvStatus.text = "Role Not Assigned"
        }
    }
}