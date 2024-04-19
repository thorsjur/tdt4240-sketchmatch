package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.communication.ResponseEvent
import com.groupfive.sketchmatch.communication.dto.response.AnswerToGuessResponseDTO
import com.groupfive.sketchmatch.models.Event
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class GuessViewModel: ViewModel(){
    private val client = MessageClient.getInstance()
    val isCorrect: MutableLiveData<Boolean> = MutableLiveData()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
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

            if (guess.isCorrect) {
                sendEvent(Event.CorrectGuessEvent)
            } else {
                sendEvent(Event.IncorrectGuessEvent)
            }
        }
    }

    fun checkGuess(inputGuess: String, gameRoomId: Int) {
        client.checkGuess(inputGuess, gameRoomId)
    }
}