package com.groupfive.sketchmatch.view.demo

import com.groupfive.sketchmatch.BuildConfig
import com.groupfive.sketchmatch.MESSAGE_EVENT
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
            socket = Socket(
                endpoint = BuildConfig.SOCKET_IO_ADDRESS,
                config = SocketOptions(
                    queryParams = null,
                    transport = SocketOptions.Transport.WEBSOCKET,
                )
            ) {
                on(SocketEvent.Connect) {
                    println("connect")
                }

                on(SocketEvent.Connecting) {
                    println("connecting")
                }

                on(SocketEvent.Disconnect) {
                    println("disconnect")
                }

                on(SocketEvent.Error) { err ->
                    println(err.message)
                }

                on(SocketEvent.Reconnect) {
                    println("reconnect")
                }

                on(SocketEvent.ReconnectAttempt) {
                    println("reconnect attempt $it")
                }

                on(SocketEvent.Ping) {
                    println("ping")
                }

                on(SocketEvent.Pong) {
                    println("pong")
                }

                on(MESSAGE_EVENT) { msg ->
                    println("Message: $msg")
                    invokeCallbacks(MESSAGE_EVENT, msg)
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
    fun sendMessage(msg: String) {
        if (!isConnected()) return
        socket.emit(MESSAGE_EVENT, msg)
    }

    fun addCallback(event: String, callback: (String) -> Unit) {
        println("$event callback added.")
        val list = eventCallbacks[event]
        if (list == null) {
            eventCallbacks[event] = mutableListOf(callback)
        } else {
            list.add { callback }
        }
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