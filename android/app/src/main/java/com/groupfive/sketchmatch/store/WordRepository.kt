package com.groupfive.sketchmatch.store

object WordRepository {
    private val easyWords =
        listOf(
            "cat",
            "sun",
            "cup",
            "dog",
            "house",
            "tree",
            "cake",
            "book",
            "flower",
            "train",
            "clock",
            "shoe"
        )
    private val mediumWords =
        listOf(
            "guitar",
            "penguin",
            "balloon",
            "elephant",
            "camera",
            "pizza",
            "skateboard",
            "beehive",
            "rainbow",
            "lighthouse",
            "kangaroo",
            "magnet"
        )
    private val hardWords =
        listOf(
            "submarine",
            "microscope",
            "chandelier",
            "tornado",
            "volcano",
            "parachute",
            "avalanche",
            "shrimp",
            "helicopter",
            "igloo",
            "koala",
            "shampoo"
        )

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
