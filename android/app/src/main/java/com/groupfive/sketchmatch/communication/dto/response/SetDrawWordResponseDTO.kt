package com.groupfive.sketchmatch.communication.dto.response

import com.groupfive.sketchmatch.models.GameRoom

data class SetDrawWordResponseDTO (
    var status: String = "",
    var message: String = "",
    var gameRoom: GameRoom? = null
)