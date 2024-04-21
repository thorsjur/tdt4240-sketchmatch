package com.groupfive.sketchmatch.communication.dto.request

data class CreateGameRequestDTO(
    val gameRoomName: String,
    val roomCapacity: Int
)