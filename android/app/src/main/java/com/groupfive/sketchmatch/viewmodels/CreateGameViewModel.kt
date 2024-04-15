package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.groupfive.sketchmatch.CREATE_ROOM
import com.groupfive.sketchmatch.ROOM_CREATED_EVENT_APPROVAL
import com.groupfive.sketchmatch.communication.dto.response.CreateGameResponseDTO
import com.groupfive.sketchmatch.view.demo.MessageClient

class CreateGameViewModel:ViewModel() {
    private val client = MessageClient.getInstance()
    val isError: MutableLiveData<Boolean> = MutableLiveData()

    init {

        // Receive confirmation/error about room created
        client.addCallback(ROOM_CREATED_EVENT_APPROVAL) { message ->
            Log.i("CreateGameViewModel", "ROOM_CREATED_EVENT_APPROVAL: $message")

            // Parse the game room from the json message string
            val gson = Gson()

            // Convert the json string to a GameRoom object
            val response = gson.fromJson(message, CreateGameResponseDTO::class.java)

            if (response.status == "success"){
                // TODO: Subscribe player when moved to lobby
            }

            else{
                isError.postValue(true)
            }
        }
    }

    fun createGameRoom(gameRoomName: String, roomCapacity: Int) {
        client.createGameRoom(CREATE_ROOM, gameRoomName, roomCapacity)
    }
}