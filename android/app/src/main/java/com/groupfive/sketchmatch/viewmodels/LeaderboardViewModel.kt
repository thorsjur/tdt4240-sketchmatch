package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.gson.Gson
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.communication.ResponseEvent
import com.groupfive.sketchmatch.communication.dto.response.GameRoomUpdateStatusResponseDTO
import com.groupfive.sketchmatch.models.Event
import com.groupfive.sketchmatch.models.GameRoomStatus
import com.groupfive.sketchmatch.navigator.Screen
import com.groupfive.sketchmatch.store.GameData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.concurrent.fixedRateTimer

class LeaderboardViewModel : ViewModel() {
    var secondsLeft by mutableIntStateOf(6)
    private val client = MessageClient.getInstance()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    init {
        client.addCallback(ResponseEvent.ROUND_FINISHED_RESPONSE.value) { message ->
            Log.i("LeaderboardViewModel", "ROUND_FINISHED_RESPONSE: $message")

            val response = Gson().fromJson(message, GameRoomUpdateStatusResponseDTO::class.java)

            GameData.currentGameRoom.postValue(response.gameRoom)

            Thread.sleep(100)

            val gameRoom = GameData.currentGameRoom.value

            val gameStatusIsChoosing = gameRoom?.gameStatus == GameRoomStatus.CHOOSING
            val playerIsDrawing = gameRoom?.getDrawingPlayerId() == GameData.currentPlayer.value?.id

            if (gameStatusIsChoosing && playerIsDrawing) {
                sendEvent(Event.NavigateDrawerToChoose)
            }
        }

        client.addCallback(ResponseEvent.ROUND_STARTED_RESPONSE.value) { message ->
            Log.i("LeaderboardViewModel", "ROUND_STARTED_RESPONSE: $message")

            val response = Gson().fromJson(message, GameRoomUpdateStatusResponseDTO::class.java)

            GameData.currentGameRoom.postValue(response.gameRoom)

            sendEvent(Event.NavigateToDraw)
        }
    }

    fun clearAllCallbacks() {
        client.removeAllCallbacks()
    }

    fun goBackToMainMenu(navController: NavController) {
        navController.popBackStack()
        clearAllCallbacks()
        navController.navigate(Screen.MainMenu.route)
    }

    fun startCountDown() = fixedRateTimer(name = "countdown", initialDelay = 500L, period = 1000L) {
        if (secondsLeft > 0) {
            secondsLeft--
        } else {
            this.cancel()
        }
    }
}

