package com.groupfive.sketchmatch.store

import androidx.lifecycle.MutableLiveData
import com.groupfive.sketchmatch.models.GameRoom
import com.groupfive.sketchmatch.models.Player

object GameData {
    val currentGameRoom: MutableLiveData<GameRoom> = MutableLiveData()
    val currentPlayer: MutableLiveData<Player> = MutableLiveData()
}