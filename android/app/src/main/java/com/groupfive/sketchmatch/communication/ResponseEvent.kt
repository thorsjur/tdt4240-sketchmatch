package com.groupfive.sketchmatch.communication

// Class defining the different types of socket events and their string representations to which clients can subscribe
enum class ResponseEvent(val value: String) {

    // Event for when server sends response of set_nickname request
    SET_NICKNAME_RESPONSE("set_nickname_response"),

    // Event for when server notifies of a new game room created
    ROOM_CREATED("game_room_created"),

    // Event for when server notifies of a game room being updated
    ROOM_UPDATED("game_room_updated"),

    // Event for when server notifies of a game room being destroyed
    ROOM_DESTROYED("game_room_destroyed"),

    // Event for when server sends a list of game rooms
    ROOMS_LIST("game_room_list"),

    // Event for when server sends response of game room creation request
    ROOM_CREATED_RESPONSE("game_room_created_response")

}