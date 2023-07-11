package com.example.virginmoney.data.models.room


import com.google.gson.annotations.SerializedName

data class RoomModel(
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isOccupied")
    val isOccupied: Boolean,
    @SerializedName("maxOccupancy")
    val maxOccupancy: Int
)