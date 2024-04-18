package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.communication.ResponseEvent
import com.groupfive.sketchmatch.communication.dto.response.TimerTickResponseDTO
import com.groupfive.sketchmatch.models.GameRoom
import com.groupfive.sketchmatch.utils.SingleLiveEvent

class RoundTimerUpdateViewModel: ViewModel() {
    private val client = MessageClient.getInstance()
    val updatedTimerTick: MutableLiveData<Int> = MutableLiveData()
    val updatedRoundGameRoom: MutableLiveData<GameRoom> = MutableLiveData()

    val roundStartedEvent = MutableLiveData<SingleLiveEvent<Unit>>()
    val roundEndedEvent = MutableLiveData<SingleLiveEvent<Unit>>()

    init {

        // Add a callback to handle incoming round_timer_update message
        client.addCallback(ResponseEvent.ROUND_TIMER_TICK_RESPONSE.value) { message ->
            Log.i("RoundTimerUpdateViewModel", "ROUND_TIMER_UPDATE_RESPONSE_EVENT: $message")

            // Parse the guess from the json message string
            val gson = Gson()

            // Convert the json string to a GameRoom object with an updated timer
            val updatedTimer = gson.fromJson(message, TimerTickResponseDTO::class.java)

            updatedTimerTick.postValue(updatedTimer.timerTick)

            // if updatedTimer.roundTimerTick == 60, then send Single LiveEvent rounds started to Leaderboard, then navigate to DrawScreenLayout
            if (updatedTimer.timerTick == 59) {
                roundStartedEvent.postValue(SingleLiveEvent(Unit))
            }
        }

        // Add a callback to handle incoming  message
        client.addCallback(ResponseEvent.ROUND_FINISHED_RESPONSE.value) { message ->
            Log.i("RoundTimerUpdateViewModel", "ROUND_FINISHED_RESPONSE_EVENT: $message")

            // Parse the guess from the json message string
            val gson = Gson()

            // Convert the json string to a GameRoom object with an updated timer

            // The RoundFinishedResponseDTO class was deleted due to rework of backend.
            // The commented logic under has to be rewritten.
            //val gameRoom = gson.fromJson(message, RoundFinishedResponseDTO::class.java)

            //updatedRoundGameRoom.postValue(gameRoom.gameRoom)

            //GameData.currentGameRoom.postValue(gameRoom.gameRoom)

            // send SingleLiveEvent round is over to DrawScreenLayout, then navigate to Leaderboard
            //roundEndedEvent.postValue(SingleLiveEvent(Unit))
        }
    }


    fun roundTimerUpdate(gameRoomId: Int) {
        client.roundTimerUpdate(gameRoomId)
    }
}