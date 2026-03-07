package com.example.uploadingscreen.repository

import com.example.uploadingscreen.model.AvailableRoomResponse
import com.example.uploadingscreen.model.CreateRoomResponse
import com.example.uploadingscreen.model.RoomLookupResponse
import com.example.uploadingscreen.utils.Resource
import com.example.uploadingscreen.network.RetrofitClient

class RoomRepository {
    suspend fun createRoom(token:String): Resource<CreateRoomResponse>{
        return try{
            val response = RetrofitClient.api.createRoom("Bearer $token")
            if(response.isSuccessful && response.body()!=null){
                Resource.Success(response.body()!!)
            } else{
                Resource.Error("Failed to create Room")
            }
        } catch (e:Exception){
            Resource.Error("Network Error :${e.message}")
        }
    }

    suspend fun getAvailableRooms(token:String): Resource<List<AvailableRoomResponse>>{
        return try {
            val response = RetrofitClient.api.getAvailableRooms("Bearer $token")
            if(response.isSuccessful && response.body()!=null){
                Resource.Success(response.body()!!)
            } else{
                Resource.Error("Failed to fetch rooms")
            }
        } catch(e:Exception){
            Resource.Error("Network Error: ${e.message}")
        }
    }

    suspend fun lookupRoom(token:String,code:String): Resource<RoomLookupResponse>{
        return try{
            val response = RetrofitClient.api.lookUpRoom("Bearer $token",code)
            if(response.isSuccessful && response.body()!= null){
                Resource.Success(response.body()!!)
            }else{
                Resource.Error("Room Not Found")
            }
        } catch (e:Exception){
            Resource.Error("Network Error: ${e.message}")
        }
    }
}