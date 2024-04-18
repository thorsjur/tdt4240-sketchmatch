package com.groupfive.sketchmatch.communication.dto.response

data class CheckGuessResponseDTO (
    var inputGuess: String = "",
    var isCorrect: Boolean = false
)