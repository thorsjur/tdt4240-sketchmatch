package com.groupfive.sketchmatch.communication

// Class defining the different types of socket events and their string representations which clients can send to the server
enum class RequestEvent(val value: String) {

    // Event for when a user joins a room
    SET_NICKNAME("set_nickname"),

    // Event for when a user creates a room
    CREATE_ROOM("create_room"),

    // Event for when a user gets a list of rooms
    GET_ROOMS("get_game_rooms"),

    // Event for when a user wants to join a room by code
    JOIN_ROOM_BY_CODE("join_room_by_code"),

    // Event for publishing a path to a room
    PUBLISH_PATH("publish_path"),

    // Event for subscribing to a specific room
    SUBSCRIBE_TO_ROOM("subscribe_to_room"),
    
    // Event for unsubscribing from a specific room
    UNSUBSCRIBE_FROM_ROOM("unsubscribe_from_room"),

}