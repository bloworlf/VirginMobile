package com.example.virginmoney.ui.adapters

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.virginmoney.R
import com.example.virginmoney.ui.holders.RoomHolder
import com.example.virginmoney.data.models.room.RoomModel
import com.example.virginmoney.utils.Utils.colorTransition
import com.example.virginmoney.utils.Utils.shake
import com.google.android.material.card.MaterialCardView
import java.lang.Exception
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                (holder.itemView as MaterialCardView).elevation = -1F
            }
            holder.container.colorTransition(Color.parseColor("#eeeeee"))
            "Occupied"
        } else {
            holder.container.colorTransition(
                ResourcesCompat.getColor(
                    context.resources,
                    R.color.background,
                    null
                )
            )
            "Available"
        }
        holder.createdAt.text = try {
            SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                Locale.getDefault()
            ).parse(room.createdAt).toString()
        } catch (e: Exception) {
            "-"
        }

        holder.itemView.setOnClickListener {
            if (room.isOccupied) {
                holder.itemView.shake()
                Toast.makeText(context, "Sorry, this room is occupied", Toast.LENGTH_SHORT).show()
            } else {
                //
            }
        }
    }
}