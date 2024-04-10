package com.groupfive.sketchmatch.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.concurrent.fixedRateTimer

class GuessingViewModel : ViewModel() {
    // How this state will be updated is TBD
    var userGuess by mutableStateOf("")
    var currentRound by mutableStateOf(1)
    var totalNumberOfRounds by mutableStateOf(5)
    var secondsLeft by mutableStateOf(56)
    var guessWordLength by mutableStateOf(7)

    /**
     * The countdown will probably be handled in some other way, however this can stay for demonstration
     * purposes.
     **/
    fun startCountDown() = fixedRateTimer(name = "countdown", initialDelay = 0L, period = 1000L) {
        if (secondsLeft > 0) secondsLeft-- else this.cancel()
    }
}