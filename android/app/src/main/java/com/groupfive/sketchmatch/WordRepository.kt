package com.groupfive.sketchmatch

object WordRepository {
    val easyWords = listOf("cat", "sun", "cup")
    val mediumWords = listOf("guitar", "penguin", "balloon")
    val hardWords = listOf("submarine", "microscope", "chandelier")

    fun getRandomWord(difficulty: Difficulty): String {
        return when (difficulty) {
            Difficulty.EASY -> easyWords.random()
            Difficulty.MEDIUM -> mediumWords.random()
            Difficulty.HARD -> hardWords.random()
        }
    }
}

enum class Difficulty {
    EASY, MEDIUM, HARD
}
