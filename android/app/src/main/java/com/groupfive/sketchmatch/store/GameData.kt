package com.groupfive.sketchmatch.store

import androidx.lifecycle.MutableLiveData
import com.groupfive.sketchmatch.models.GameRoom
import com.groupfive.sketchmatch.models.Player
import io.ak1.drawbox.DrawBoxPayLoad

object GameData {
    val currentGameRoom: MutableLiveData<GameRoom> = MutableLiveData()
    val currentPlayer: MutableLiveData<Player> = MutableLiveData()
    val drawBoxPayLoad: MutableLiveData<DrawBoxPayLoad> = MutableLiveData()
    val lastGuessCorrectness: MutableLiveData<Boolean> = MutableLiveData()
}