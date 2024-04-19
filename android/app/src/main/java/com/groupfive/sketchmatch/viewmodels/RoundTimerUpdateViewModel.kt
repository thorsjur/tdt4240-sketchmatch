package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.communication.ResponseEvent
import com.groupfive.sketchmatch.communication.dto.response.TimerTickResponseDTO

class RoundTimerUpdateViewModel: ViewModel() {
    private val client = MessageClient.getInstance()
    val updatedTimerTick: MutableLiveData<Int> = MutableLiveData()

    init {

        // Add a callback to handle incoming round_timer_update message
        client.addCallback(ResponseEvent.ROUND_TIMER_TICK_RESPONSE.value) { message ->
            Log.i("RoundTimerUpdateViewModel", "ROUND_TIMER_UPDATE_RESPONSE_EVENT: $message")

            // Convert the json string to a GameRoom object with an updated timer
            val updatedTimer = Gson().fromJson(message, TimerTickResponseDTO::class.java)

            updatedTimerTick.postValue(updatedTimer.timerTick)
        }

        /*
        // Add a callback to handle incoming  message
        client.addCallback(ResponseEvent.ROUND_FINISHED_RESPONSE.value) { message ->
            Log.i("RoundTimerUpdateViewModel", "ROUND_FINISHED_RESPONSE_EVENT: $message")

            // Parse the guess from the json message string
            val gson = Gson()
        }

         */
    }
}