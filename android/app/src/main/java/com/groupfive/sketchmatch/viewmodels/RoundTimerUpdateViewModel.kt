package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.communication.ResponseEvent
import com.groupfive.sketchmatch.communication.dto.response.RoundFinishedResponseDTO
import com.groupfive.sketchmatch.communication.dto.response.RoundTimerUpdateResponseDTO
import com.groupfive.sketchmatch.models.GameRoom
import com.groupfive.sketchmatch.store.GameData

class RoundTimerUpdateViewModel: ViewModel() {
    private val client = MessageClient.getInstance()
    val updatedTimerTick: MutableLiveData<Int> = MutableLiveData()
    val updatedRoundGameRoom: MutableLiveData<GameRoom> = MutableLiveData()

    init {

        // Add a callback to handle incoming round_timer_update message
        client.addCallback(ResponseEvent.ROUND_TIMER_UPDATE_RESPONSE.value) { message ->
            Log.i("RoundTimerUpdateViewModel", "ROUND_TIMER_UPDATE_RESPONSE_EVENT: $message")

            // Parse the guess from the json message string
            val gson = Gson()

            // Convert the json string to a GameRoom object with an updated timer
            val updatedTimer = gson.fromJson(message, RoundTimerUpdateResponseDTO::class.java)

            updatedTimerTick.postValue(updatedTimer.roundTimerTick)

            // if updatedTimer.roundTimerTick == 60, then send Single LiveEvent rounds started to Leaderboard, then navigate to DrawScreenLayout
        }

        // Add a callback to handle incoming  message
        client.addCallback(ResponseEvent.ROUND_FINISHED_RESPONSE.value) { message ->
            Log.i("RoundTimerUpdateViewModel", "ROUND_FINISHED_RESPONSE_EVENT: $message")

            // Parse the guess from the json message string
            val gson = Gson()

            // Convert the json string to a GameRoom object with an updated timer
            val gameRoom = gson.fromJson(message, RoundFinishedResponseDTO::class.java)

            updatedRoundGameRoom.postValue(gameRoom.gameRoom)

            GameData.currentGameRoom.postValue(gameRoom.gameRoom)

            // send SingleLiveEvent round is over to DrawScreenLayout, then navigate to Leaderboard
        }
    }


    fun roundTimerUpdate(gameRoomId: Int) {
        client.roundTimerUpdate(gameRoomId)
    }
}