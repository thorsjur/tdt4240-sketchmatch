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
    var drawingPlayer: Int = 0
    var gameStatus: GameRoomStatus = GameRoomStatus.WAITING
    var roundTimestamp: Int = 60
    var word: String = ""
    var guessedCorrectly: List<String> = emptyList()

    fun getCurrentRoundNumber(): Int {
        return drawingPlayer
    }

    fun getTotalNumberOfRounds(): Int {
        return players.size;
    }

    fun getDrawingPlayerId(): String {
        if (drawingPlayer >= players.size) {
            return "Unknown"
        }

        return players[drawingPlayer].id
    }

    fun getDrawingPlayerName(): String {
        if (drawingPlayer >= players.size) {
            return ""
        }

        return players[drawingPlayer].nickname
    }

    fun getCurrentWordMask() = "_ ".repeat(word.length)
}

// Enum class for game room status
enum class GameRoomStatus {
    WAITING,
    PLAYING,
    FINISHED,
    CHOOSING,
    LEADERBOARD
}