package com.example.virginmoney.network

import com.example.virginmoney.models.people.PeopleModel
import com.example.virginmoney.utils.Constants
import retrofit2.http.GET

interface PeopleCall {

    @GET(Constants.PEOPLE_ENDPOINT)
    suspend fun getPeople(): ArrayList<PeopleModel>
}
