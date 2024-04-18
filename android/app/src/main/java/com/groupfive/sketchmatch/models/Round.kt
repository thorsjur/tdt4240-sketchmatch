package com.groupfive.sketchmatch.models

import com.groupfive.sketchmatch.Difficulty

/**
 * Represents a round within a game
 */
class Round {
    var drawWord: String = ""
    var difficulty: Difficulty = Difficulty.EASY
    var drawingPlayer: Player? = null
    var timer: Int = 60
}