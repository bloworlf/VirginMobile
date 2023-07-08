package com.example.virginmoney.network

import com.example.virginmoney.models.room.RoomModel
import com.example.virginmoney.utils.Constants
import retrofit2.http.GET

interface RoomsCall {

    @GET(Constants.ROOM_ENDPOINT)
    suspend fun getRooms(): ArrayList<RoomModel>
}
