package com.groupfive.sketchmatch

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
    if (roundNumber == 1) {
        return "Score after 1 round"
    } else if (roundNumber < numberOfRounds) {
        return "Score after $roundNumber rounds"
    } else {
        return "Final score"
    }
}