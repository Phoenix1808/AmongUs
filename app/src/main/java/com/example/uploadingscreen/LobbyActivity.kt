package com.example.uploadingscreen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import android.widget.Button
import android.widget.ProgressBar
import com.example.uploadingscreen.utils.Resource
import com.example.uploadingscreen.viewmodel.RoomViewModel
import android.widget.TextView

class LobbyActivity : AppCompatActivity() {
    private lateinit var viewModel : RoomViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        viewModel = ViewModelProvider(this)[RoomViewModel::class.java]

        val btnCreateRoom = findViewById<Button>(R.id.btnCreateRoom)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tvRoomCode = findViewById<TextView>(R.id.tvRoomCode)

        btnCreateRoom.setOnClickListener {

            val token = getSharedPreferences("auth", MODE_PRIVATE)
                .getString("token", null)

            if (token != null) {
                viewModel.createRoom(token)
            } else {
                Toast.makeText(this, "Token missing", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.createRoom.observe(this){resource->
            when(resource){
                is Resource.Loading->{
                   progressBar.visibility = View.VISIBLE
                    btnCreateRoom.isEnabled = false
                }
                is Resource.Success ->{
                    progressBar.visibility = View.GONE
                    btnCreateRoom.isEnabled = true
                    val roomCode = resource.data?.code
                    tvRoomCode.text = "Room Code: $roomCode"
                }
                is Resource.Error ->{
                    progressBar.visibility = View.GONE
                    btnCreateRoom.isEnabled = true

                    Toast.makeText(this,resource.message ?: "Error Creating Room",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}