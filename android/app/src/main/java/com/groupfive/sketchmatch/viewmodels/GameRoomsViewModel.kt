package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.groupfive.sketchmatch.models.GameRoom
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.communication.RequestEvent
import com.groupfive.sketchmatch.communication.ResponseEvent
import com.groupfive.sketchmatch.communication.dto.request.JoinGameByCodeRequestDTO
import com.groupfive.sketchmatch.communication.dto.response.JoinGameResponseDTO
import com.groupfive.sketchmatch.utils.SingleLiveEvent

class GameRoomsViewModel : ViewModel() {
    val gameRooms: MutableLiveData<List<GameRoom>> = MutableLiveData()
    private val client = MessageClient.getInstance()

    val joinGameByCodeStatus: MutableLiveData<Boolean> = MutableLiveData()
    val joinGameByCodeMessage: MutableLiveData<String> = MutableLiveData()

    val navigateToGameLobby = MutableLiveData<SingleLiveEvent<Unit>>()

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

            Log.i("GameRoomsViewModel", "New game room created: ${room.id}")

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

        // Add a callback to handle incoming join_room_by_code_response messages
        client.addCallback(ResponseEvent.JOIN_ROOM_RESPONSE.value) { message ->
            Log.i("GameRoomsViewModel", "JOIN_ROOM_RESPONSE: $message")

            // Parse the game room from the json message string
            val gson = Gson()

            // Convert the json string to a GameRoom object
            val response = gson.fromJson(message, JoinGameResponseDTO::class.java)

            joinGameByCodeMessage.postValue(response.message)

            if (response.status == "success") {
                Log.i("GameRoomsViewModel", "Joined room with code ${response.gameRoom?.gameCode}")

                // TODO: Save the game room data to the GameData singleton object

                joinGameByCodeStatus.postValue(true)
                navigateToGameLobby.postValue(SingleLiveEvent(Unit))  // Trigger the navigation event

                // TODO: Navigate to the game room screen
            } else {
                // Display an error message
            }
        }

        // Request the list of game rooms from the server
        refreshGameRooms()
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

    // Refresh the list of game rooms
    fun refreshGameRooms() {
        client.sendMessage(RequestEvent.GET_ROOMS.value)
    }

    // Update an existing game room
    fun updateGameRoom(room: GameRoom) {
        try {
            val currentRooms = gameRooms.value.orEmpty().toMutableList()
            val index = currentRooms.indexOfFirst { it.id == room.id }
            if (index != -1) {
                currentRooms[index] = room
                gameRooms.postValue(currentRooms)
                Log.i("GameRoomsViewModel", "Game room updated: ${room.id}")
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

    // Send a request to the server to join a game by code
    fun joinGameByCode(code: String) {
        // Create JoinGameByCodeRequestDTO object
        val dto = JoinGameByCodeRequestDTO(code)

        // Convert the DTO object to a JSON string
        val jsonData = Gson().toJson(dto)

        client.sendMessage(RequestEvent.JOIN_ROOM_BY_CODE.value, jsonData)
    }

    // Remove all callbacks
    fun removeAllCallbacks() {
        client.removeAllCallbacks(ResponseEvent.ROOMS_LIST.value)
        client.removeAllCallbacks(ResponseEvent.SET_NICKNAME_RESPONSE.value)
        client.removeAllCallbacks(ResponseEvent.ROOM_UPDATED.value)
        client.removeAllCallbacks(ResponseEvent.ROOM_DESTROYED.value)
    }
}