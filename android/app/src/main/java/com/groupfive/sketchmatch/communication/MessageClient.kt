package com.groupfive.sketchmatch.communication

import android.util.Log
import com.google.gson.Gson
import com.groupfive.sketchmatch.BuildConfig
import com.groupfive.sketchmatch.communication.dto.request.CheckGuessRequestDTO
import com.groupfive.sketchmatch.communication.dto.request.CreateGameRequestDTO
import com.groupfive.sketchmatch.communication.dto.request.PublishPathRequestDTO
import com.groupfive.sketchmatch.communication.dto.request.SetDrawWordRequestDTO
import com.groupfive.sketchmatch.serialization.DrawBoxPayLoadSerializer
import com.groupfive.sketchmatch.store.Difficulty
import dev.icerock.moko.socket.Socket
import dev.icerock.moko.socket.SocketEvent
import dev.icerock.moko.socket.SocketOptions
import io.ak1.drawbox.DrawBoxPayLoad
import kotlinx.serialization.json.Json
import java.net.URISyntaxException

/**
 * A client for communicating with the SocketIO server implementation.
 *
 * Currently using the singleton pattern to ensure only one message client exists
 * per mobile client.
 */
class MessageClient private constructor(
    private val uuid: String? = null
) {

    companion object {

        @Volatile
        private var instance: MessageClient? = null

        fun getInstance(uuid: String? = null) =
            instance ?: synchronized(this) {
                instance ?: MessageClient(uuid).also { instance = it }
            }
    }

    private lateinit var socket: Socket
    private val eventCallbacks: MutableMap<String, MutableList<(String) -> Unit>> = mutableMapOf()
    private val gson = Gson()

    init {
        setSocket()
    }

    @Synchronized
    private fun setSocket() {
        try {
            Log.i("Socket", "Setting up socket with address: ${BuildConfig.SOCKET_IO_ADDRESS}")

            val queryParams = mutableMapOf<String, String>()

            // Add the hardware ID to the query parameters
            uuid?.let { queryParams["hwid"] = it }

            socket = Socket(
                endpoint = BuildConfig.SOCKET_IO_ADDRESS,
                config = SocketOptions(
                    queryParams = queryParams,
                    transport = SocketOptions.Transport.WEBSOCKET,
                )
            ) {
                on(SocketEvent.Connect) {
                    println("connect")
                    Log.i("Socket", "Connected")
                }

                on(SocketEvent.Connecting) {
                    println("connecting")
                    Log.i("Socket", "Connecting")
                }

                on(SocketEvent.Disconnect) {
                    println("disconnect")
                    Log.i("Socket", "Disconnected")
                }

                on(SocketEvent.Error) { err ->
                    println(err.message)
                    err.message?.let { Log.e("Socket", it) }
                }

                on(SocketEvent.Reconnect) {
                    println("reconnect")
                    Log.i("Socket", "Reconnected")
                }

                on(SocketEvent.ReconnectAttempt) {
                    println("reconnect attempt $it")
                    Log.i("Socket", "Reconnect attempt $it")
                }

                on(SocketEvent.Ping) {
                    println("ping")
                    Log.i("Socket", "Ping")
                }

                on(SocketEvent.Pong) {
                    println("pong")
                    Log.i("Socket", "Pong")
                }

                // On SET_NICKNAME_RESPONSE_EVENT
                on(ResponseEvent.SET_NICKNAME_RESPONSE.value) { msg ->
                    invokeCallbacks(ResponseEvent.SET_NICKNAME_RESPONSE.value, msg)
                }

                // On ROOMS_LIST_EVENT
                on(ResponseEvent.ROOMS_LIST.value) { msg ->
                    invokeCallbacks(ResponseEvent.ROOMS_LIST.value, msg)
                }

                // On ROOM_CREATED_EVENT
                on(ResponseEvent.ROOM_CREATED.value) { msg ->
                    invokeCallbacks(ResponseEvent.ROOM_CREATED.value, msg)
                }

                // On ROOM_CREATED_EVENT_APPROVAL
                on(ResponseEvent.ROOM_CREATED_RESPONSE.value) { msg ->
                    Log.i("Socket", "Room created: $msg")
                    invokeCallbacks(ResponseEvent.ROOM_CREATED_RESPONSE.value, msg)
                }

                // On ROOM_UPDATED_EVENT
                on(ResponseEvent.ROOM_UPDATED.value) { msg ->
                    Log.i("Socket", "Room updated: $msg")
                    invokeCallbacks(ResponseEvent.ROOM_UPDATED.value, msg)
                }

                // On ROOM_DESTROYED_EVENT
                on(ResponseEvent.ROOM_DESTROYED.value) { msg ->
                    invokeCallbacks(ResponseEvent.ROOM_DESTROYED.value, msg)
                }

                // On SET_DRAW_WORD_RESPONSE_EVENT
                on(ResponseEvent.SET_DRAW_WORD_RESPONSE.value) { msg ->
                    invokeCallbacks(ResponseEvent.SET_DRAW_WORD_RESPONSE.value, msg)
                }

                // On ROUND_TIMER_UPDATE_RESPONSE_EVENT
                on(ResponseEvent.ROUND_TIMER_TICK_RESPONSE.value) { msg ->
                    invokeCallbacks(ResponseEvent.ROUND_TIMER_TICK_RESPONSE.value, msg)
                }

                on(ResponseEvent.LEADERBOARD_TIMER_TICK_RESPONSE.value) { msg ->
                    invokeCallbacks(ResponseEvent.LEADERBOARD_TIMER_TICK_RESPONSE.value, msg)
                }

                on(ResponseEvent.ANSWER_TO_GUESS_RESPONSE.value) { msg ->
                    invokeCallbacks(ResponseEvent.ANSWER_TO_GUESS_RESPONSE.value, msg)
                }

                on(ResponseEvent.ROUND_STARTED_RESPONSE.value) { msg ->
                    invokeCallbacks(ResponseEvent.ROUND_STARTED_RESPONSE.value, msg)
                }

                on(ResponseEvent.OPEN_LEADERBOARD_RESPONSE.value) { msg ->
                    invokeCallbacks(ResponseEvent.OPEN_LEADERBOARD_RESPONSE.value, msg)
                }

                // On ROUND_FINISHED_RESPONSE_EVENT
                on(ResponseEvent.ROUND_FINISHED_RESPONSE.value) { msg ->
                    invokeCallbacks(ResponseEvent.ROUND_FINISHED_RESPONSE.value, msg)
                }

                // On MESSAGE_PUBLISHED_IN_SUBSCRIBED_ROOM
                on(ResponseEvent.DRAW_PAYLOAD_PUBLISHED.value) { msg ->
                    invokeCallbacks(ResponseEvent.DRAW_PAYLOAD_PUBLISHED.value, msg)
                }

                // On JOIN_ROOM_BY_CODE_RESPONSE
                on(ResponseEvent.JOIN_ROOM_RESPONSE.value) { msg ->
                    invokeCallbacks(ResponseEvent.JOIN_ROOM_RESPONSE.value, msg)
                }

                // On ROUND_IS_CREATED_RESPONSE
                on(ResponseEvent.ROUND_IS_CREATED_RESPONSE.value) { msg ->
                    Log.i("Socket", "Round is created: $msg")
                    invokeCallbacks(ResponseEvent.ROUND_IS_CREATED_RESPONSE.value, msg)
                }

                // On PLAYER_JOINED_ROOM event
                on(ResponseEvent.PLAYER_JOINED_ROOM.value) { msg ->
                    Log.i("Socket", "Player joined room: $msg")
                    invokeCallbacks(ResponseEvent.PLAYER_JOINED_ROOM.value, msg)
                }

                // On PLAYER_LEFT_ROOM event
                on(ResponseEvent.PLAYER_LEFT_ROOM.value) { msg ->
                    Log.i("Socket", "Player left room: $msg")
                    invokeCallbacks(ResponseEvent.PLAYER_LEFT_ROOM.value, msg)
                }
            }
        } catch (e: URISyntaxException) {

        }
    }

    @Synchronized
    fun establishConnection() {
        socket.connect()
    }

    @Synchronized
    fun sendMessage(eventName: String, msg: String = "") {
        if (!isConnected()) return
        socket.emit(eventName, msg)
    }

    @Synchronized
    fun publishFullDrawBoxPayload(roomId: Int, payLoad: DrawBoxPayLoad) {
        val stringifiedPayload = Json.encodeToString(DrawBoxPayLoadSerializer, payLoad)
        val request = PublishPathRequestDTO(roomId, stringifiedPayload)
        val data = gson.toJson(request)

        sendMessage(RequestEvent.PUBLISH_PATH.value, data)
    }

    @Synchronized
    fun createGameRoom(gameRoomName: String, roomCapacity: Int) {
        val requestData = CreateGameRequestDTO(gameRoomName, roomCapacity)
        val data = gson.toJson(requestData)

        if (!isConnected()) return
        socket.emit(
            RequestEvent.CREATE_ROOM.value,
            data
        )
    }

    @Synchronized
    fun checkGuess(inputGuess: String, gameRoomId: Int) {
        val requestData = CheckGuessRequestDTO(inputGuess, gameRoomId)
        val gson = Gson()
        val data = gson.toJson(requestData)

        if (!isConnected()) return
        socket.emit(
            RequestEvent.CHECK_GUESS.value,
            data
        )
    }

    @Synchronized
    fun setDrawWord(drawWord: String, difficulty: Difficulty, gameRoomId: Int) {
        val requestData = SetDrawWordRequestDTO(drawWord, difficulty, gameRoomId)

        val data = Gson().toJson(requestData)

        if (!isConnected()) return
        socket.emit(RequestEvent.SET_DRAW_WORD.value, data)
    }

    fun addCallback(event: String, callback: (String) -> Unit) {
        println("$event callback added.")
        Log.i("Socket", "$event callback added.")
        val list = eventCallbacks[event]
        if (list == null) {
            eventCallbacks[event] = mutableListOf(callback)
        } else {
            list.add(callback)
        }
    }

    // Remove all callbacks for a specific event
    fun removeAllCallbacks(event: String) {
        eventCallbacks.remove(event)
    }

    fun removeAllCallbacks() {
        eventCallbacks.clear()
    }

    private fun isConnected() =
        socket.isConnected()

    private fun invokeCallbacks(event: String, msg: String) {
        val callbacks = eventCallbacks[event]
        callbacks?.forEach {
            it.invoke(msg)
        }
    }
}
