package com.groupfive.sketchmatch.utils

fun getPlayerRankString(rank: Int): String {
    when (rank) {
        5 -> return "5th"
        4 -> return "4th"
        3 -> return "3rd"
        2 -> return "2nd"
        1 -> return "1st"
    }
    return "Something wrong has happened"
}

fun getRoundString(roundNumber: Int, numberOfRounds: Int): String {
    return if (roundNumber == 1) {
        "Score after 1 round"
    } else {
        "Score after $roundNumber rounds"
    }
}