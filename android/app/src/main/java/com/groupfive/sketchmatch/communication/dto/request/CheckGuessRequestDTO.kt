package com.groupfive.sketchmatch.communication.dto.request

data class CheckGuessRequestDTO (
    var inputGuess: String,
    var gameRoomId: Int,
)