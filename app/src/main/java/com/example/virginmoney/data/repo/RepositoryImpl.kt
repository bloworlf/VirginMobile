package com.example.virginmoney.data.repo

import com.example.virginmoney.models.people.PeopleModel
import com.example.virginmoney.models.room.RoomModel
import com.example.virginmoney.network.PeopleCall
import com.example.virginmoney.network.RoomsCall
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    val peopleCall: PeopleCall,
    val roomsCall: RoomsCall
) : Repository {

    override suspend fun getPeople(): ArrayList<PeopleModel> = peopleCall.getPeople()

    override suspend fun getRooms(): ArrayList<RoomModel> = roomsCall.getRooms()
}