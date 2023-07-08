package com.example.virginmoney.data.repo

import com.example.virginmoney.models.people.PeopleModel
import com.example.virginmoney.models.room.RoomModel

interface Repository {

    suspend fun getPeople(): ArrayList<PeopleModel>

    suspend fun getRooms(): ArrayList<RoomModel>
}