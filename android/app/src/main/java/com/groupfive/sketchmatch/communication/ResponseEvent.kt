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
    ROOM_CREATED_RESPONSE("game_room_created_response"),

    // Event for when server notifies of a guess being checked
    CHECK_GUESS_RESPONSE("check_guess_response"),

    // Event for when server sends response of game room join by code request
    JOIN_ROOM_RESPONSE("join_room_response"),

    // Event for when server notifies that a draw word has been selected
    SET_DRAW_WORD_RESPONSE("set_draw_word_response"),

    // Event for when server notifies of the timer for a round starting
    ROUND_TIMER_UPDATE_RESPONSE("round_timer_update_response"),

    // Event for when server notifies of a round finishing
    ROUND_FINISHED_RESPONSE("round_finished_response"),

    // Event for when server publishes to a room the client is subscribed to
    DRAW_PAYLOAD_PUBLISHED("draw_payload_published"),
    
    // Event for when server notifies of the timer being updated
    UPDATE_TIMER_RESPONSE("update_timer_response"),

    // Event for when server notifies of a player joining a game room
    PLAYER_JOINED_ROOM("player_joined_room")
}