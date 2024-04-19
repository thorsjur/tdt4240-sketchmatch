
import { EventEmitter } from "events";

export class Round extends EventEmitter{
    constructor(sequenceNumber, gameRoomId, drawingPlayer) {
        super();
        this.sequenceNumber = sequenceNumber; // The number in the sequence of rounds that this round is (eg. 1 would be the first round in the game)
        this.gameRoomId = gameRoomId; // The game room that this round is in
        this.drawingPlayer = drawingPlayer; // The player who is drawing
        this.drawWord = ""; // The word that is being drawn
        this.difficulty = "EASY"; // Default difficulty
        this.timer = 60; // 60 seconds
    }

    getSequenceNumber() {
        return this.sequenceNumber;
    }

    getDrawingPlayer() {
        return this.drawingPlayer;
    }

    getDrawWord() {
        return this.drawWord;
    }

    setDrawWord(drawWord) {
        this.drawWord = drawWord;
    }

    getDifficulty() {
        return this.difficulty;
    }

    setDifficulty(difficulty) {
        this.difficulty = difficulty;
    }

    getTimer() {
        return this.timer;
    }

    setTimer(timer) {
        this.timer = timer;
    }


    startRound(gameRoom) {
        let counter;
        counter = setInterval(() => {
            this.timer--;
            this.emit('timer_tick', this.timer, gameRoom);
          
            // Stop the timer on 0
            if (this.timer == 0) {
                clearInterval(counter);
                gameRoom.updateStatus();
                this.emit('round_finished', gameRoom);
            }
        }, 1000);
    }

    calculateGuesserScore(timestamp) {
        // The earlier the player guesses the word, the more points they get
        return timestamp >= 45
            ? 100
            : timestamp >= 30
            ? 75
            : timestamp >= 15
            ? 50
            : timestamp > 0
            ? 25
            : 0;
    }

    calculateDrawerScore() {
        let difficultyPoints = 5; // default points for easy difficulty, max is 5*4 = 20

        switch (this.difficulty) {
            case "medium":
                difficultyPoints = 15; // max points for medium difficulty is 15*4 = 60
                break;
            case "hard":
                difficultyPoints = 25; // max points for hard difficulty is 25*4 = 100
                break;
        }
        return difficultyPoints;
    }

    setDrawingPlayer(player) {
        this.drawingPlayer = player;
    }

    getGameRoomId() {
        return this.gameRoomId;
    }
}
