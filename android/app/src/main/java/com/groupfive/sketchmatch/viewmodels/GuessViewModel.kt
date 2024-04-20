package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.communication.ResponseEvent
import com.groupfive.sketchmatch.communication.dto.response.AnswerToGuessResponseDTO
import com.groupfive.sketchmatch.communication.dto.response.PayloadResponseDTO
import com.groupfive.sketchmatch.models.Event
import com.groupfive.sketchmatch.serialization.DrawBoxPayLoadSerializer
import com.groupfive.sketchmatch.store.GameData
import io.ak1.drawbox.DrawController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class GuessViewModel : ViewModel() {
    private val client = MessageClient.getInstance()
    var isCorrect: Boolean = false

    private val _eventsFlow = MutableSharedFlow<Event>()
    val eventsFlow = _eventsFlow.asSharedFlow()

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            _eventsFlow.emit(event)
        }
    }

    fun clearAllCallbacks() {
        client.removeAllCallbacks()
    }

    init {
        // Add a callback to handle incoming guess_updated message
        client.addCallback(ResponseEvent.ANSWER_TO_GUESS_RESPONSE.value) { message ->
            Log.i("GuessViewModel", "ANSWER_TO_GUESS_RESPONSE: $message")

            // Convert the json string to a Guess object
            val guess = Gson().fromJson(message, AnswerToGuessResponseDTO::class.java)

            //isCorrect.postValue(guess.isCorrect)
            GameData.lastGuessCorrectness.postValue(true)
            GameData.currentGameRoom.postValue(guess.gameRoom)

            if(guess.playerId == GameData.currentPlayer.value?.id) {
                isCorrect = guess.isCorrect

                // Wait for 2 seconds before sending the GuessAnswerEvent
                viewModelScope.launch {
                    sendEvent(Event.GuessAnswerEvent)
                }
            }
        }
    }

    fun checkGuess(inputGuess: String, gameRoomId: Int) {
        client.checkGuess(inputGuess, gameRoomId)
    }

    fun handleRender(controller: DrawController) {
        controller.reset()
        GameData.drawBoxPayLoad.postValue(null)
        
        client.addCallback(ResponseEvent.DRAW_PAYLOAD_PUBLISHED.value) {
            Log.i("GuessViewModel", "DRAW_PAYLOAD_PUBLISHED: $it")

            val gson = Gson()
            val response = gson.fromJson(it, PayloadResponseDTO::class.java)
            val drawBoxPayLoad =
                Json.decodeFromString(DrawBoxPayLoadSerializer, response.pathPayload)

            GameData.drawBoxPayLoad.postValue(drawBoxPayLoad)
        }
    }

    fun handleDestroy() = client.removeAllCallbacks(ResponseEvent.DRAW_PAYLOAD_PUBLISHED.value)
}