package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.communication.ResponseEvent
import com.groupfive.sketchmatch.communication.dto.response.GameRoomUpdateStatusResponseDTO
import com.groupfive.sketchmatch.models.NavigationEvent
import com.groupfive.sketchmatch.models.Player
import com.groupfive.sketchmatch.store.GameData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.concurrent.fixedRateTimer

// TODO: Remove the dummy values and standard player list and round number
val p1 = Player("1","hwid1", "Ola", 15)
val p2 = Player("2", "hwid1","Kari", 18)
val p3 = Player("3", "hwid1","Arne", 27)
val p4 = Player("4", "hwid1","Gunn", 24)
val p5 = Player("5", "hwid1","Åse", 19)

class LeaderboardViewModel : ViewModel() {
    var secondsLeft by mutableIntStateOf(6)
    private val client = MessageClient.getInstance()

    private val eventChannel = Channel<NavigationEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun sendEvent(event: NavigationEvent) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }
    init {
        client.addCallback(ResponseEvent.ROUND_STARTED_RESPONSE.value) { message ->
            Log.i("LobbyViewModel", "ROUND_IS_STARTED_RESPONSE: $message")

            val response = Gson().fromJson(message, GameRoomUpdateStatusResponseDTO::class.java)

            GameData.currentGameRoom.postValue(response.gameRoom)
        })
    }

    fun startCountDown() = fixedRateTimer(name = "countdown", initialDelay = 500L, period = 1000L) {
        if (secondsLeft > 0) {
            secondsLeft--
        } else {
            this.cancel()
        }
    }
}

