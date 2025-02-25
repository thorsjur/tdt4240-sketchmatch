package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.communication.RequestEvent
import com.groupfive.sketchmatch.communication.ResponseEvent
import com.groupfive.sketchmatch.communication.dto.response.GameRoomUpdateStatusResponseDTO
import com.groupfive.sketchmatch.communication.dto.response.JoinGameResponseDTO
import com.groupfive.sketchmatch.models.Event
import com.groupfive.sketchmatch.store.GameData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class WaitingLobbyViewModel : ViewModel() {
    private val client = MessageClient.getInstance()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun clearAllCallbacks() {
        client.removeAllCallbacks()
    }

    init {
        client.addCallback(ResponseEvent.PLAYER_JOINED_ROOM.value) { message ->
            Log.i("LobbyViewModel", "PLAYER_JOINED_ROOM: $message")

            // Convert the json string to a list of GameRoom objects
            val response = Gson().fromJson(message, JoinGameResponseDTO::class.java)

            GameData.currentGameRoom.postValue(response.gameRoom)
        }

        client.addCallback(ResponseEvent.PLAYER_LEFT_ROOM.value) { message ->
            Log.i("LobbyViewModel", "PLAYER_LEFT_ROOM: $message")

            // Convert the json string to a list of GameRoom objects
            val response = Gson().fromJson(message, JoinGameResponseDTO::class.java)

            GameData.currentGameRoom.postValue(response.gameRoom)
        }

        // Add a callback for when the round is created
        client.addCallback(ResponseEvent.ROUND_IS_CREATED_RESPONSE.value) { message ->
            Log.i("LobbyViewModel", "ROUND_IS_CREATED_RESPONSE: $message")

            // Convert the json string to a list of GameRoom objects
            val response = Gson().fromJson(message, GameRoomUpdateStatusResponseDTO::class.java)

            GameData.currentGameRoom.postValue(response.gameRoom)

            val drawingPlayerID = GameData.currentGameRoom.value?.getDrawingPlayerId()
            val playerId = GameData.currentPlayer.value?.id

            if (drawingPlayerID == playerId) {
                sendEvent(Event.NavigateToDraw)
            }
        }

        client.addCallback(ResponseEvent.ROUND_STARTED_RESPONSE.value) { message ->
            Log.i("LobbyViewModel", "ROUND_IS_STARTED_RESPONSE: $message")

            val response = Gson().fromJson(message, GameRoomUpdateStatusResponseDTO::class.java)

            GameData.currentGameRoom.postValue(response.gameRoom)

            sendEvent(Event.NavigateToDraw)
        }
    }

    fun leaveGameRoom() {
        client.sendMessage(RequestEvent.LEAVE_ROOM.value)
    }
}
