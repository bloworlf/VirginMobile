package com.example.virginmoney.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.virginmoney.R

class RoomHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val roomNumber: TextView
    val maxOccupancy: TextView
    val isOccupied: TextView
    val createdAt: TextView

    init {
        roomNumber = itemView.findViewById(R.id.roomNumber)
        maxOccupancy = itemView.findViewById(R.id.maxOccupancy)
        isOccupied = itemView.findViewById(R.id.isOccupied)
        createdAt = itemView.findViewById(R.id.createdAt)
    }
}
