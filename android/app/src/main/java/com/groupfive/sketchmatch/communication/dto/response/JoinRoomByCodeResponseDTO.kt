package com.groupfive.sketchmatch.communication.dto.response

import com.groupfive.sketchmatch.models.GameRoom

class JoinRoomByCodeResponseDTO {
    val message: String = ""
    val status: String = ""
    val gameRoom: GameRoom = GameRoom()
}