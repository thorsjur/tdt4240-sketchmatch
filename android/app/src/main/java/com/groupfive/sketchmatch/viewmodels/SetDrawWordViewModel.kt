package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.communication.ResponseEvent
import com.groupfive.sketchmatch.communication.dto.response.SetDrawWordResponseDTO
import com.groupfive.sketchmatch.store.Difficulty
import com.groupfive.sketchmatch.store.GameData

class SetDrawWordViewModel : ViewModel() {
    private val client = MessageClient.getInstance()

    init {
        // Add a callback to handle incoming guess_updated message
        client.addCallback(ResponseEvent.SET_DRAW_WORD_RESPONSE.value) { message ->
            Log.i("SetDrawWordViewModel", "SET_DRAW_WORD_RESPONSE_EVENT: $message")

            // Convert the json string to a GameRoom object with the draw word
            val gameRoom = Gson().fromJson(message, SetDrawWordResponseDTO::class.java)
        }
    }


    fun setDrawWord(drawWord: String, difficulty: Difficulty) {
        Log.i(
            "SetDrawWordViewModel",
            "Sending the selected draw word to server: $drawWord, $difficulty, ${GameData.currentGameRoom.value?.id}"
        )
        client.setDrawWord(drawWord, difficulty, GameData.currentGameRoom.value?.id ?: 0)
    }
}