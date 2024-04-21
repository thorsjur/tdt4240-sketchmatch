package com.groupfive.sketchmatch.communication.dto.request

import com.groupfive.sketchmatch.store.Difficulty

data class SetDrawWordRequestDTO(
    val drawWord: String = "null",
    val difficulty: Difficulty = Difficulty.EASY,
    val gameRoomId: Int = 1
)