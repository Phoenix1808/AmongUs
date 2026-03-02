package com.example.uploadingscreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uploadingscreen.utils.Resource
import com.example.uploadingscreen.model.CreateRoomResponse
import com.example.uploadingscreen.repository.RoomRepository
import kotlinx.coroutines.launch

class RoomViewModel: ViewModel() {
    private val repo = RoomRepository()

    private val _createRoom = MutableLiveData<Resource<CreateRoomResponse>>()
    val createRoom : LiveData<Resource<CreateRoomResponse>> = _createRoom

    fun createRoom(token:String){
        viewModelScope.launch{
            _createRoom.value = Resource.Loading()
            _createRoom.value = repo.createRoom(token)
        }

    }
}