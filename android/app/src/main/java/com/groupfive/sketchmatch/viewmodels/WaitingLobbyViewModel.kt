package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.communication.ResponseEvent
import com.groupfive.sketchmatch.communication.dto.response.JoinGameResponseDTO
import com.groupfive.sketchmatch.store.GameData
import kotlinx.coroutines.flow.MutableStateFlow

class WaitingLobbyViewModel: ViewModel() {
    private val _timerFinished = MutableStateFlow(false)
    private val client = MessageClient.getInstance()

    init {
        client.addCallback(ResponseEvent.PLAYER_JOINED_ROOM.value) { message ->
            Log.i("LobbyViewModel", "PLAYER_JOINED_ROOM: $message")

            // Parse the game rooms from the json message string
            val gson = Gson()

            // Convert the json string to a list of GameRoom objects
            val response = gson.fromJson(message, JoinGameResponseDTO::class.java)

            GameData.currentGameRoom.postValue(response.gameRoom)
        }
    }
}
