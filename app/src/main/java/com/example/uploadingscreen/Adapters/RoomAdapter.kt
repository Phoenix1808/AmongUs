package com.example.uploadingscreen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uploadingscreen.R
import com.example.uploadingscreen.model.AvailableRoomResponse

class RoomAdapter(
    private var rooms: List<AvailableRoomResponse>,
    private val onClick: (AvailableRoomResponse) -> Unit
) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRoomCode: TextView = itemView.findViewById(R.id.tvRoomCode)
        val tvHost: TextView = itemView.findViewById(R.id.tvHost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rom, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]

        holder.tvRoomCode.text = "Code: ${room.code}"
        holder.tvHost.text = "Host: ${room.host.username}"

        holder.itemView.setOnClickListener {
            onClick(room)
        }
    }

    override fun getItemCount() = rooms.size

    fun updateData(newRooms: List<AvailableRoomResponse>) {
        rooms = newRooms
        notifyDataSetChanged()
    }
}