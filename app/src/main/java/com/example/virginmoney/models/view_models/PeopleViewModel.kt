package com.example.virginmoney.models.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.virginmoney.data.repo.Repository
import com.example.virginmoney.models.people.PeopleModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    val liveData: MutableLiveData<ArrayList<PeopleModel>> by lazy {
        MutableLiveData<ArrayList<PeopleModel>>()
    }

    fun getPeople() {
        CoroutineScope(Dispatchers.Main).launch {
            val value = repo.getPeople()
            liveData.postValue(value)
        }
    }
}