package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.communication.ResponseEvent
import com.groupfive.sketchmatch.models.GameRoom
import com.groupfive.sketchmatch.store.GameData

class GameRoomViewModel : ViewModel() {

    private val client = MessageClient.getInstance()

    init {
        // Add a callback to handle incoming room_updated messages
        client.addCallback(ResponseEvent.ROOM_UPDATED.value) { message ->
            Log.i("GameRoomViewModel", "ROOM_UPDATED_EVENT: $message")

            // Parse the game room from the json message string
            val gson = Gson()

            // Convert the json string to a GameRoom object
            val room = gson.fromJson(message, GameRoom::class.java)

            // Update the current game room
            updateCurrentGameRoom(room)
        }

        // Updates the list of players in the global state to reflect who has guessed correctly,
        // it is based on their hwid.
        client.addCallback(ResponseEvent.PLAYER_GUESSED_CORRECTLY.value) { msg ->
            val gameRoom = GameData.currentGameRoom
            val currentValue = gameRoom.value
            if (currentValue != null) {
                val gson = Gson()
                val hwid = gson.fromJson(msg, String::class.java)

                gameRoom.postValue(currentValue.apply {
                    players = players.filterNotNull().map { player ->
                        if (player.hwid == hwid) player.apply {
                            hasGuessedCorrectly = true
                        } else {
                            return@map player
                        }
                    }
                })

            }
        }
    }

    private fun updateCurrentGameRoom(room: GameRoom) = GameData
        .currentGameRoom.postValue(room)
}