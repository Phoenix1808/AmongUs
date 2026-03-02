package com.example.uploadingscreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uploadingscreen.model.AvailableRoomResponse
import com.example.uploadingscreen.utils.Resource
import com.example.uploadingscreen.model.CreateRoomResponse
import com.example.uploadingscreen.model.RoomLookupResponse
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

    private val _availableRooms = MutableLiveData<Resource<List<AvailableRoomResponse>>>()
    val availableRooms : LiveData<Resource<List<AvailableRoomResponse>>> = _availableRooms

    fun getAvailableRooms(token:String){
        viewModelScope.launch{
            _availableRooms.value = Resource.Loading()
            _availableRooms.value = repo.getAvailableRooms(token)
        }
    }

    private val _lookupRoom = MutableLiveData<Resource<RoomLookupResponse>>()
    val lookupRoom : LiveData<Resource<RoomLookupResponse>> = _lookupRoom

    fun lookupRoom(token:String,code:String){
        viewModelScope.launch{
            _lookupRoom.value = Resource.Loading()
            _lookupRoom.value = repo.lookupRoom(token,code)
        }
    }
}