package com.groupfive.sketchmatch.models

data class Player(
    var id: String = "",
    var hwid: String = "",
    var nickname: String = "",
    var score: Int = 0,
    var hasGuessedCorrectly: Boolean = false
)
