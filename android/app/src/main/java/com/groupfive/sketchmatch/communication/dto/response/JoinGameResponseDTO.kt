package com.groupfive.sketchmatch.communication.dto.response

import com.groupfive.sketchmatch.models.GameRoom

data class JoinGameResponseDTO (
    val message: String = "",
    val status: String = "",
    val gameRoom: GameRoom = GameRoom()
)