package com.groupfive.sketchmatch.communication.dto.request

class CreateGameRequestDTO(gameRoomName: String, roomCapacity: Int) {
    val gameRoomName: String = gameRoomName
    val roomCapacity: Int = roomCapacity
}