package com.groupfive.sketchmatch.models

open class Event {
    object NavigateToDraw : Event()
    object NavigateToLeaderboard : Event()
    object NavigateDrawerToChoose : Event()
    object NavigateToWaitingLobby : Event()
    object CorrectGuessEvent : Event()
    object IncorrectGuessEvent : Event()
    object RoomErrorEvent : Event()
    object RoomSuccessEvent : Event()
    object GuessAnswerEvent : Event()
}