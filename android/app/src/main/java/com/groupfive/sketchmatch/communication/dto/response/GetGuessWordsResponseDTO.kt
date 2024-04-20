package com.groupfive.sketchmatch.communication.dto.response

import com.groupfive.sketchmatch.models.GuessWord

class GetGuessWordsResponseDTO {
    var status: String = ""
    var message: String = ""
    lateinit var guessWords: List<GuessWord>
}