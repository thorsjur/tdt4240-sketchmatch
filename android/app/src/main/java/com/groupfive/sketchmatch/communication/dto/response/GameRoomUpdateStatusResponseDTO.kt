package com.groupfive.sketchmatch.communication.dto.response

import com.groupfive.sketchmatch.models.GameRoom

data class GameRoomUpdateStatusResponseDTO (
    val gameRoom: GameRoom,
    val message: String,
    val status: String
)