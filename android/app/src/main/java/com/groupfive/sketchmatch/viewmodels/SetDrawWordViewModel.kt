package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.groupfive.sketchmatch.Difficulty
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.communication.ResponseEvent
import com.groupfive.sketchmatch.communication.dto.response.SetDrawWordResponseDTO

class SetDrawWordViewModel: ViewModel() {
    private val client = MessageClient.getInstance()

    init {

        // Add a callback to handle incoming guess_updated message
        client.addCallback(ResponseEvent.SET_DRAW_WORD_RESPONSE.value) { message ->
            Log.i("SetDrawWordViewModel", "SET_DRAW_WORD_RESPONSE_EVENT: $message")

            // Parse the guess from the json message string
            val gson = Gson()

            // Convert the json string to a GameRoom object with the draw word
            val gameRoom = gson.fromJson(message, SetDrawWordResponseDTO::class.java)
        }
    }


    fun setDrawWord(drawWordId: String) {
        client.setDrawWord(drawWordId)
    }
}