package com.example.uploadingscreen

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.uploadingscreen.utils.Resource
import com.example.uploadingscreen.viewmodel.RoomViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uploadingscreen.adapter.RoomAdapter
class LobbyActivity : AppCompatActivity() {

    private lateinit var viewModel: RoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        val rvRooms = findViewById<RecyclerView>(R.id.rvRooms)

        rvRooms.layoutManager = LinearLayoutManager(this)

        val adapter = RoomAdapter(emptyList()) { room ->
            // On click auto lookup
            val token = getSharedPreferences("auth", MODE_PRIVATE)
                .getString("token", null)

            if (token != null) {
                viewModel.lookupRoom(token, room.code)
            }
        }

        rvRooms.adapter = adapter

        viewModel = ViewModelProvider(this)[RoomViewModel::class.java]

        val btnCreateRoom = findViewById<Button>(R.id.btnCreateRoom)
        val btnFetchRoom = findViewById<Button>(R.id.btnFetchRoom)
        val btnLookupRoom = findViewById<Button>(R.id.btnLookupRoom)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val tvCreateResult = findViewById<TextView>(R.id.tvCreateResult)
//        val tvAvailableResult = findViewById<TextView>(R.id.tvAvailableResult)
        val tvLookupResult = findViewById<TextView>(R.id.tvLookupResult)

        val etRoomCode = findViewById<EditText>(R.id.etRoomCode)

        val token = getSharedPreferences("auth", MODE_PRIVATE)
            .getString("token", null)

        // Create Room
        btnCreateRoom.setOnClickListener {
            if (token != null) {
                viewModel.createRoom(token)
            } else {
                Toast.makeText(this, "Token missing", Toast.LENGTH_SHORT).show()
            }
        }

        // Get Available Rooms
        btnFetchRoom.setOnClickListener {
            if (token != null) {
                viewModel.getAvailableRooms(token)
            } else {
                Toast.makeText(this, "Token missing", Toast.LENGTH_SHORT).show()
            }
        }

        // Lookup Room
        btnLookupRoom.setOnClickListener {
            val code = etRoomCode.text.toString().trim()

            if (token == null) {
                Toast.makeText(this, "Token missing", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (code.isEmpty()) {
                etRoomCode.error = "Enter valid room code"
                return@setOnClickListener
            }

            viewModel.lookupRoom(token, code)
        }

        // Observers

        viewModel.createRoom.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    tvCreateResult.text =
                        "Created Room Code: ${resource.data?.code}"
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.availableRooms.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    val rooms = resource.data
                    if (!rooms.isNullOrEmpty()) {
                        adapter.updateData(rooms)
                    }
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.lookupRoom.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    tvLookupResult.text =
                        "Lookup Success: ${resource.data?.code}"
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}