package com.groupfive.sketchmatch.communication.dto.response

import com.groupfive.sketchmatch.models.GameRoom

data class AnswerToGuessResponseDTO (
    val isCorrect: Boolean,
    val playerId: String,
    val gameRoom: GameRoom
)