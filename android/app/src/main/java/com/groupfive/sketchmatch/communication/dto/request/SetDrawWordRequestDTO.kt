package com.groupfive.sketchmatch.communication.dto.request

import com.groupfive.sketchmatch.Difficulty

class SetDrawWordRequestDTO (
    var drawWord: String = "null",
    var difficulty: Difficulty = Difficulty.EASY,
    var gameRoomId: Int = 1)
{
}