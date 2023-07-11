package com.example.virginmoney.data.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.virginmoney.data.repo.Repository
import com.example.virginmoney.data.models.room.RoomModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomsViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    val liveData: MutableLiveData<ArrayList<RoomModel>> by lazy {
        MutableLiveData<ArrayList<RoomModel>>()
    }

    fun getRooms() {
        CoroutineScope(Dispatchers.Main).launch {
            val value = repo.getRooms()
            liveData.postValue(value)
        }
    }
}