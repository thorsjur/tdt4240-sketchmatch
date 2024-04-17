package com.groupfive.sketchmatch.communication.dto.response

import com.groupfive.sketchmatch.models.Player

class SetNicknameResponseDTO {
    var status: String = ""
    var message: String = ""
    var player: Player? = null
}