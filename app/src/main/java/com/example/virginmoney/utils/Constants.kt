package com.example.virginmoney.utils

object Constants {

    const val BASE_API: String = "https://61e947967bc0550017bc61bf.mockapi.io/api/v1/"

    private const val PEOPLE: String = "people"
    private const val ROOMS: String = "rooms"

    const val PEOPLE_ENDPOINT = "$BASE_API$PEOPLE"
    const val ROOM_ENDPOINT = "$BASE_API$ROOMS"
}