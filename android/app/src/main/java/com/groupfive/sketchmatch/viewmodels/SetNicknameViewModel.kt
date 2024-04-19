package com.groupfive.sketchmatch.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.communication.RequestEvent
import com.groupfive.sketchmatch.communication.ResponseEvent
import com.groupfive.sketchmatch.communication.dto.request.SetNicknameRequestDTO
import com.groupfive.sketchmatch.communication.dto.response.SetNicknameResponseDTO
import com.groupfive.sketchmatch.store.GameData

class SetNicknameViewModel: ViewModel() {
    private val client = MessageClient.getInstance()
    val nicknameSetStatus: MutableLiveData<Boolean> = MutableLiveData()
    val nicknameSetMessage: MutableLiveData<String> = MutableLiveData()
    val nickname: MutableLiveData<String> = MutableLiveData()

    init {

        // Subscribe to SET_NICKNAME_RESPONSE_EVENT to handle nickname setting responses
        client.addCallback(ResponseEvent.SET_NICKNAME_RESPONSE.value) { message ->
            Log.i("SetNicknameViewModel", "SET_NICKNAME_RESPONSE_EVENT: $message")

            // Parse the response from the server
            val response = Gson().fromJson(message, SetNicknameResponseDTO::class.java)

            if (response.status == "success") {
                // Set the current player in the GameData store
                GameData.currentPlayer.postValue(response.player)

                // Set the nickname in the view model
                nickname.postValue(response.player?.nickname ?: "")

                // Set the nickname set status to true
                nicknameSetStatus.postValue(true)
            } else {
                // Set the nickname set status to false
                nicknameSetStatus.postValue(false)
            }
            nicknameSetMessage.postValue(response.message)
        }

    }

    // Send a request to set the user's nickname
    fun setNickname(nickname: String) {
        // Creating DTO object to send to the server
        val nicknameDTO = SetNicknameRequestDTO(nickname)

        // Convert the DTO object to a JSON string
        val jsonData = Gson().toJson(nicknameDTO)

        Log.i("SetNicknameViewModel", "Sending nickname: $jsonData")

        client.sendMessage(RequestEvent.SET_NICKNAME.value, jsonData)
    }
}