package com.groupfive.sketchmatch.communication.dto.response

data class AnswerToGuessResponseDTO (
    val isCorrect: Boolean,
    val playerId: String
)