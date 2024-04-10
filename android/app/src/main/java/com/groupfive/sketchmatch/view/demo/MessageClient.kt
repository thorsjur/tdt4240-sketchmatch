package com.groupfive.sketchmatch.view.demo

import android.util.Log
import com.groupfive.sketchmatch.BuildConfig
import com.groupfive.sketchmatch.MESSAGE_EVENT
import com.groupfive.sketchmatch.ROOMS_LIST_EVENT
import com.groupfive.sketchmatch.ROOM_CREATED_EVENT
import com.groupfive.sketchmatch.ROOM_DESTROYED_EVENT
import com.groupfive.sketchmatch.ROOM_UPDATED_EVENT
import dev.icerock.moko.socket.Socket
import dev.icerock.moko.socket.SocketEvent
import dev.icerock.moko.socket.SocketOptions
import java.net.URISyntaxException

/**
 * A client for communicating with the SocketIO server implementation.
 *
 * Currently using the singleton pattern to ensure only one message client exists
 * per mobile client.
 */
class MessageClient private constructor() {

    companion object {

        @Volatile
        private var instance: MessageClient? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: MessageClient().also { instance = it }
            }
    }

    private lateinit var socket: Socket
    private val eventCallbacks: MutableMap<String, MutableList<(String) -> Unit>> = mutableMapOf()

    init {
        setSocket()
    }

    @Synchronized
    private fun setSocket() {
        try {
            Log.i("Socket", "Setting up socket with address: ${BuildConfig.SOCKET_IO_ADDRESS}")
            socket = Socket(
                endpoint = BuildConfig.SOCKET_IO_ADDRESS,
                config = SocketOptions(
                    queryParams = null,
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

                on(MESSAGE_EVENT) { msg ->
                    println("Message: $msg")
                    Log.i("Socket", "Message: $msg")
                    invokeCallbacks(MESSAGE_EVENT, msg)
                }

                // On ROOMS_LIST_EVENT
                on(ROOMS_LIST_EVENT) { msg ->
                    invokeCallbacks(ROOMS_LIST_EVENT, msg)
                }

                // On ROOM_CREATED_EVENT
                on(ROOM_CREATED_EVENT) { msg ->
                    invokeCallbacks(ROOM_CREATED_EVENT, msg)
                }

                // On ROOM_UPDATED_EVENT
                on(ROOM_UPDATED_EVENT) { msg ->
                    invokeCallbacks(ROOM_UPDATED_EVENT, msg)
                }

                // On ROOM_DESTROYED_EVENT
                on(ROOM_DESTROYED_EVENT) { msg ->
                    invokeCallbacks(ROOM_DESTROYED_EVENT, msg)
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
    fun closeConnection() {
        socket.disconnect()
    }

    @Synchronized
    fun sendMessage(eventName: String, msg: String = "") {
        if (!isConnected()) return
        socket.emit(eventName, msg)
    }

    fun addCallback(event: String, callback: (String) -> Unit) {
        println("$event callback added.")
        Log.i("Socket", "$event callback added.")
        val list = eventCallbacks[event]
        if (list == null) {
            eventCallbacks[event] = mutableListOf(callback)
        } else {
            list.add { callback }
        }
    }

    fun removeCallback(event: String, callback: (String) -> Unit) {
        val list = eventCallbacks[event]
        list?.remove(callback)
    }

    fun removeAllCallbacks(event: String) {
        eventCallbacks.remove(event)
    }

    fun unsubscribeFromEvent(event: String) {
        // TODO: Implement unsubscribeFromEvent if the library supports it
        // We need to unsubscribe the socket from the event to prevent receiving unwanted messages
        // E.g. when the user navigates from Game Rooms screen to another screen and we don't want to receive room updates anymore.
    }

    fun subscribeToEvent(event: String) {
        // TODO: Implement subscribeToEvent if the library supports it
        // We need to subscribe back to the event to start receiving messages again
        // E.g. when the user navigates back to the Game Rooms screen and we want to receive room updates again.
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
