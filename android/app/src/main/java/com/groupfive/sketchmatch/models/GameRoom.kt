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
    var gameStatus: GameRoomStatus = GameRoomStatus.Waiting
    var drawWord: String = ""
    var rounds: MutableList<Round> = mutableListOf() // A round consists of a number of turns
    var currentRound: Int = 1 // The current round being played

    fun getCurrentRound(): Round {
        return rounds[currentRound - 1]
    }
}

// Enum class for game room status
enum class GameRoomStatus {
    Waiting,
    Playing,
    Finished
}