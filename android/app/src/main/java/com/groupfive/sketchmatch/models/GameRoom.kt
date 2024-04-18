package com.groupfive.sketchmatch.models

/**
 * Represents a game room in the application.
 */
class GameRoom {
    var id: Int = 0
    var gameCode: String = ""
    var gameName: String = ""
    var gameCapacity: Int = 2
    var players: List<Player> = emptyList()
    var drawingPLayer: Int = 0
    var gameStatus: GameRoomStatus = GameRoomStatus.WAITING
    var roundTimestamp: Int = 60
    var word: String = ""
    var guessedCorrectly: List<String> = emptyList()

    fun getCurrentRoundNumber(): Int {
        return drawingPLayer + 1
    }

    fun getDrawingPlayerId(): String {
        return players[drawingPLayer].id
    }

    fun getDrawingPlayerName(): String {
        return players[drawingPLayer].nickname
    }
}

// Enum class for game room status
enum class GameRoomStatus {
    WAITING,
    PLAYING,
    FINISHED,
    CHOOSING,
    LEADERBOARD
}