package com.example.virginmoney.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.virginmoney.R
import com.example.virginmoney.holders.RoomHolder
import com.example.virginmoney.models.room.RoomModel
import java.text.SimpleDateFormat
import java.util.Locale

class RoomAdapter(
    private val context: Context,
    var list: ArrayList<RoomModel>,
    navController: NavController
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.model_room, parent, false)
        return RoomHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(h0: RecyclerView.ViewHolder, position: Int) {
        val room = list[position]
        val holder = h0 as RoomHolder

        holder.roomNumber.text = "Room #${room.id}"
        holder.maxOccupancy.text = "Max occupancy: ${room.maxOccupancy.toString()}"
        holder.isOccupied.text = if (room.isOccupied) {
            "Occupied"
        } else {
            "Available"
        }
        holder.createdAt.text = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault()
        ).parse(room.createdAt).toString()

        holder.itemView.setOnClickListener {
            if (room.isOccupied) {
                Toast.makeText(context, "Sorry, this room is occupied", Toast.LENGTH_SHORT).show()
            } else {
                //
            }
        }
    }
}