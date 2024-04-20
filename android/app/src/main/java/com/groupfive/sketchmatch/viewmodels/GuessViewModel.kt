package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class GuessViewModel : ViewModel() {
    private val client = MessageClient.getInstance()
    val isCorrect: MutableLiveData<Boolean> = MutableLiveData()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun clearAllCallbacks() {
        client.removeAllCallbacks()
    }

    init {
        // Add a callback to handle incoming guess_updated message
        client.addCallback(ResponseEvent.ANSWER_TO_GUESS_RESPONSE.value) { message ->
            Log.i("GuessViewModel", "ANSWER_TO_GUESS_RESPONSE: $message")

            // Parse the guess from the json message string
            val gson = Gson()

            // Convert the json string to a Guess object
            val guess = gson.fromJson(message, AnswerToGuessResponseDTO::class.java)

            isCorrect.postValue(guess.isCorrect)

            GameData.lastGuessCorrectness.postValue(true)

            GameData.currentGameRoom.postValue(guess.gameRoom)
        }
    }

    fun checkGuess(inputGuess: String, gameRoomId: Int) {
        client.checkGuess(inputGuess, gameRoomId)
    }

    fun handleRender(controller: DrawController) =
        client.addCallback(ResponseEvent.DRAW_PAYLOAD_PUBLISHED.value) {
            Log.i("GuessViewModel", "DRAW_PAYLOAD_PUBLISHED: $it")

            val gson = Gson()
            val response = gson.fromJson(it, PayloadResponseDTO::class.java)
            val drawBoxPayLoad =
                Json.decodeFromString(DrawBoxPayLoadSerializer, response.pathPayload)
            //controller.importPath(drawBoxPayLoad)

            GameData.drawBoxPayLoad.postValue(drawBoxPayLoad)

            ///sendEvent(NavigationEvent.NewDrawingPayload)
        }

    fun handleDestroy() = client.removeAllCallbacks(ResponseEvent.DRAW_PAYLOAD_PUBLISHED.value)
}