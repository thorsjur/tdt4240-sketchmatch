package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.groupfive.sketchmatch.models.GameRoom
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.communication.RequestEvent
import com.groupfive.sketchmatch.communication.ResponseEvent

class GameRoomsViewModel : ViewModel() {
    val gameRooms: MutableLiveData<List<GameRoom>> = MutableLiveData()
    private val client = MessageClient.getInstance()

    init {
        // Initialize the list of game rooms with fake data
        populateFakeGameRooms()

        // Add a callback to handle incoming rooms_list messages
        client.addCallback(ResponseEvent.ROOMS_LIST.value) { message ->
            Log.i("GameRoomsViewModel", "ROOMS_LIST_EVENT: $message")

            // Parse the game rooms from the json message string
            val gson = Gson()

            // Convert the json string to a list of GameRoom objects
            val rooms = gson.fromJson(message, Array<GameRoom>::class.java).toList()

            // Populate the list with the new data
            populateGameRooms(rooms)
        }

        // Add a callback to handle incoming room_created messages
        client.addCallback(ResponseEvent.ROOM_CREATED.value) { message ->
            Log.i("GameRoomsViewModel", "ROOM_CREATED_EVENT: $message")

            // Parse the game room from the json message string
            val gson = Gson()

            // Convert the json string to a GameRoom object
            val room = gson.fromJson(message, GameRoom::class.java)

            // Add the new game room to the list
            addGameRoom(room)
        }

        // Add a callback to handle incoming room_updated messages
        client.addCallback(ResponseEvent.ROOM_UPDATED.value) { message ->
            Log.i("GameRoomsViewModel", "ROOM_UPDATED_EVENT: $message")

            // Parse the game room from the json message string
            val gson = Gson()

            // Convert the json string to a GameRoom object
            val room = gson.fromJson(message, GameRoom::class.java)

            // Update the game room in the list
            updateGameRoom(room)
        }

        // Add a callback to handle incoming room_destroyed messages
        client.addCallback(ResponseEvent.ROOM_DESTROYED.value) { message ->
            Log.i("GameRoomsViewModel", "ROOM_DESTROYED_EVENT: $message")

            // Parse the game room from the json message string
            val gson = Gson()

            // Convert the json string to a GameRoom object
            val room = gson.fromJson(message, GameRoom::class.java)

            // Remove the game room from the list
            removeGameRoom(room)
        }

        // Request the list of game rooms from the server
        client.sendMessage(RequestEvent.GET_ROOMS.value)
    }

    // Populate the list with new data
    fun populateGameRooms(rooms: List<GameRoom>) {
        gameRooms.postValue(rooms)
    }

    // Populate the list with fake data
    private fun populateFakeGameRooms() {
        gameRooms.postValue(
            listOf(
                GameRoom().apply { id = 1 },
                GameRoom().apply { id = 2 },
                GameRoom().apply { id = 3 },
                GameRoom().apply { id = 4 },
                GameRoom().apply { id = 5 },
                GameRoom().apply { id = 6 },
                GameRoom().apply { id = 7 },
                GameRoom().apply { id = 8 },
                GameRoom().apply { id = 9 },
                GameRoom().apply { id = 10 },
                GameRoom().apply { id = 11 },
                GameRoom().apply { id = 12 },
                GameRoom().apply { id = 13 },
                GameRoom().apply { id = 14 },
                GameRoom().apply { id = 15 },
                GameRoom().apply { id = 16 },
                GameRoom().apply { id = 17 },
                GameRoom().apply { id = 18 },
                GameRoom().apply { id = 19 },
                GameRoom().apply { id = 20 }
            )
        )
    }

    // Add a new game room to the list
    fun addGameRoom(room: GameRoom) {
        val currentRooms = gameRooms.value.orEmpty().toMutableList()
        currentRooms.add(room)
        gameRooms.postValue(currentRooms)
    }

    // Update an existing game room
    fun updateGameRoom(room: GameRoom) {
        try {
            val currentRooms = gameRooms.value.orEmpty().toMutableList()
            val index = currentRooms.indexOfFirst { it.id == room.id }
            if (index != -1) {
                currentRooms[index] = room
                gameRooms.postValue(currentRooms)
            }
        } catch (e: Exception) {
            Log.e("GameRoomsViewModel", "Error updating game room: ${e.message}")
        }
    }

    // Remove a game room from the list
    fun removeGameRoom(room: GameRoom) {
        try {
            val currentRooms = gameRooms.value.orEmpty().toMutableList()
            val index = currentRooms.indexOfFirst { it.id == room.id }
            currentRooms.removeAt(index)
            gameRooms.postValue(currentRooms)
        } catch (e: Exception) {
            Log.e("GameRoomsViewModel", "Error removing game room: ${e.message}")
        }
    }

    // Remove all callbacks
    fun removeAllCallbacks() {
        client.removeAllCallbacks(ResponseEvent.ROOMS_LIST.value)
        client.removeAllCallbacks(ResponseEvent.SET_NICKNAME_RESPONSE.value)
        client.removeAllCallbacks(ResponseEvent.ROOM_UPDATED.value)
        client.removeAllCallbacks(ResponseEvent.ROOM_DESTROYED.value)
    }
}