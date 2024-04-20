package com.groupfive.sketchmatch.communication.dto.response

import com.groupfive.sketchmatch.models.GameRoom

data class CreateGameResponseDTO (
    val message: String = "",
    val status: String = "",
    val gameRoom: GameRoom? = null
)