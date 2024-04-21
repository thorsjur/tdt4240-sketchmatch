package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.communication.ResponseEvent
import com.groupfive.sketchmatch.communication.dto.response.CreateGameResponseDTO
import com.groupfive.sketchmatch.models.Event
import com.groupfive.sketchmatch.store.GameData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CreateGameViewModel : ViewModel() {
    private val client = MessageClient.getInstance()
    val isError: MutableLiveData<Boolean> = MutableLiveData()

    private val _eventsFlow = MutableSharedFlow<Event>()
    val eventsFlow = _eventsFlow.asSharedFlow()

    private fun sendEvent(event: Event) {
        viewModelScope.launch {
            _eventsFlow.emit(event)
        }
    }

    fun clearAllCallbacks() {
        client.removeAllCallbacks()
    }

    init {

        // Receive confirmation/error about room created
        client.addCallback(ResponseEvent.ROOM_CREATED_RESPONSE.value) { message ->
            Log.i("CreateGameViewModel", "ROOM_CREATED_EVENT_APPROVAL: $message")

            // Parse the game room from the json message string
            val gson = Gson()

            // Convert the json string to a GameRoom object
            val response = gson.fromJson(message, CreateGameResponseDTO::class.java)

            if (response.status == "success") {
                // Update the current game room
                GameData.currentGameRoom.postValue(response.gameRoom)

                Log.i("CreateGameViewModel", "Game room created: ${response.gameRoom}")

                // Navigate to the draw screen
                sendEvent(Event.NavigateToWaitingLobby)

                // TODO: Subscribe player when moved to lobby
            } else {
                isError.postValue(true)
            }
        }
    }

    fun createGameRoom(gameRoomName: String, roomCapacity: Int) {
        client.createGameRoom(gameRoomName, roomCapacity)
    }
}