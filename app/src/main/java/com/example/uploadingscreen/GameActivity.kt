package com.example.uploadingscreen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.uploadingscreen.databinding.ActivityGameBinding
import com.example.uploadingscreen.network.SocketManager
import com.google.android.gms.location.*
import org.json.JSONObject
import java.io.Serializable
import java.util.HashMap

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    private val playerMap = mutableMapOf<String, String>()

    private var roomCode: String? = null
    private var role: String? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    companion object {
        private const val LOCATION_PERMISSION_REQUEST = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userIds = intent.getStringArrayExtra("userIds")
        val usernames = intent.getStringArrayExtra("usernames")
        if (userIds != null && usernames != null) {
            for (i in userIds.indices) {
                playerMap[userIds[i]] = usernames[i]
            }
        }

        roomCode = intent.getStringExtra("roomCode")
        role = intent.getStringExtra("role")

        binding.tvRoomCode.text = "Room Code : $roomCode"

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

        listenForPlayerMovement()
        listenForTargets()

        setupLocation()
    }


    private fun setupLocation() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {

            override fun onLocationResult(result: LocationResult) {

                val location = result.lastLocation ?: return

                val lat = location.latitude
                val lng = location.longitude

                sendMove(lat, lng)

                binding.tvMyPosition.text = "My Position: $lat , $lng"

                Log.d("REAL_GPS", "$lat,$lng")
            }
        }

        requestLocationPermission()
    }

    private fun requestLocationPermission() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )

        } else {

            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            2000
        )
            .setMinUpdateIntervalMillis(1000)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationClient.requestLocationUpdates(
                request,
                locationCallback,
                mainLooper
            )
        }
    }

    private fun sendMove(lat: Double, lng: Double) {

        val socket = SocketManager.getSocket() ?: return

        val payload = JSONObject().apply {

            put("roomCode", roomCode)

            put(
                "position",
                JSONObject().apply {
                    put("lat", lat)
                    put("lng", lng)
                }
            )
        }

        socket.emit("game:move", payload)

        Log.d("MOVE_SENT", "My position: $lat,$lng")
    }


    private fun listenForPlayerMovement() {

        val socket = SocketManager.getSocket() ?: return

        socket.off("game:player-moved")

        socket.on("game:player-moved") { args ->

            if (args.isNotEmpty() && args[0] is JSONObject) {

                val data = args[0] as JSONObject

                val userId = data.getString("userId")

                val username = playerMap[userId] ?: "Unknown"

                val position = data.getJSONObject("position")

                val lat = position.getDouble("lat")
                val lng = position.getDouble("lng")

                runOnUiThread {

                    binding.tvLastMovement.text =
                        "$username moved to $lat , $lng"

                    Log.d("PLAYER_MOVED", "$username moved to $lat,$lng")
                }
            }
        }
    }

    private fun listenForTargets() {

        val socket = SocketManager.getSocket() ?: return

        socket.off("game:nearby-targets")


        socket.on("game:nearby-targets") { args ->

            if (args.isNotEmpty() && args[0] is JSONObject) {

                val data = args[0] as JSONObject
                val targets = data.getJSONArray("targets")
                Log.d("TARGET_DEBUG", targets.toString())
                runOnUiThread {

                    val count = targets.length()

                    binding.tvNearbyPlayers.text = "Nearby Players: $count"

                    if (count > 0) {
                        Log.d("TARGETS", "Player nearby: $count")
                    } else {
                        Log.d("TARGETS", "No Players nearby")
                    }
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()

        val socket = SocketManager.getSocket()

        socket?.off("game:player-moved")
        socket?.off("game:nearby-targets")

        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}