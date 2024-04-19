package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.communication.ResponseEvent
import com.groupfive.sketchmatch.communication.dto.response.CheckGuessResponseDTO
import com.groupfive.sketchmatch.communication.dto.response.PayloadResponseDTO
import com.groupfive.sketchmatch.serialization.DrawBoxPayLoadSerializer
import io.ak1.drawbox.DrawController
import kotlinx.serialization.json.Json

class GuessViewModel : ViewModel() {
    private val client = MessageClient.getInstance()
    val isCorrect: MutableLiveData<Boolean> = MutableLiveData()

    init {

        // Add a callback to handle incoming guess_updated message
        client.addCallback(ResponseEvent.CHECK_GUESS_RESPONSE.value) { message ->
            Log.i("GuessViewModel", "GUESS_CHECKED_RESPONSE_EVENT: $message")

            // Parse the guess from the json message string
            val gson = Gson()

            // Convert the json string to a Guess object
            val guess = gson.fromJson(message, CheckGuessResponseDTO::class.java)

            isCorrect.postValue(guess.isCorrect)
        }
    }


    fun checkGuess(inputGuess: String, gameRoomId: Int, timestamp: Int) {
        client.checkGuess(inputGuess, gameRoomId, timestamp)
    }

    fun handleRender(controller: DrawController) =
        client.addCallback(ResponseEvent.DRAW_PAYLOAD_PUBLISHED.value) {
            Log.i("GuessViewModel", "DRAW_PAYLOAD_PUBLISHED: $it")
            
            val gson = Gson()
            val response = gson.fromJson(it, PayloadResponseDTO::class.java)
            val drawBoxPayLoad =
                Json.decodeFromString(DrawBoxPayLoadSerializer, response.pathPayload)
            controller.importPath(drawBoxPayLoad)
        }

    fun handleDestroy() = client.removeAllCallbacks(ResponseEvent.DRAW_PAYLOAD_PUBLISHED.value)
}