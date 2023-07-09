package com.example.virginmoney.models.people


import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PeopleModel(
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("data")
    val `data`: DataModel?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("favouriteColor")
    val favouriteColor: String?,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("fromName")
    val fromName: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("jobtitle")
    val jobtitle: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("size")
    val size: String?,
    @SerializedName("to")
    val to: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("{}")
    val x: String?
) {
//    var created: String?
//
//    init {
//        created = try {
//            println(createdAt)
//            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(createdAt)
//                ?.toString()
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
}