import { EventEmitter } from "events";
export class GameRoom extends EventEmitter {
    constructor(id, gameCode, gameName, gameCapacity = 2) {
        this.id = id;
        this.gameCode = gameCode;
        this.gameName = gameName;
        this.gameCapacity = gameCapacity;
        this.players = [];
        this.drawingPlayer = 0;
        this.gameStatus = GameStatus.Waiting;
        this.roundTimestamp = 60;
        this.word = null;
        this.wordDifficultyPoints = null;
        this.guessedCorrectly = [];
    }

    addPlayer(player) {
        this.players.push(player);
    }

    removePlayer(player) {
        this.players = this.players.filter((p) => p.id !== player.id);
    }

    // Change game status
    changeGameStatus(status) {
        this.gameStatus = status;
    }

    // Get roomId
    getRoomId() {
        return this.id;
    }

    setGameStatus(newStatus) {
        this.gameStatus = newStatus;
    }

    initializeRound(newWord, newWordDifficultyPoints) {
        this.resetRound()

        this.word = newWord;
        this.wordDifficultyPoints = newWordDifficultyPoints;
        
        this.setGameStatus(GameStatus.Playing);
        this.emit('round_has_started', this);
    }

    endDrawingPeriod() {
        this.drawingPlayer++;
        if (this.drawingPlayer + 1 > this.players.length) {
            endGame()
        }
        this.setGameStatus(GameStatus.Leaderboard);
        this.emit('open_leaderboard', this);
        startLeaderboardTimer();
    }

    handleGuess(playerId, guessedWord) {
        let player = this.players.filter(player => player.id === playerId);
        let drawer = this.players.get(this.drawingPlayer);
        let isCorrect = guessedWord === this.word;

        if (isCorrect) {
            player.incrementScore(this.calculateGuesserScore(this.roundTimestamp));
            this.guessedCorrectly.push(playerId);
            drawer.incrementScore(this.wordDifficultyPoints);
        }

        this.emit('answer_to_guess', playerId, isCorrect, this);
    }

    startRoundTimer() {
        let counter;

        counter = setInterval(() => {
            this.roundTimestamp--;
            this.emit('round_timer_tick', this);
          
            // Stop the timer on 0
            if (this.timer == 0) {
                clearInterval(counter);
                this.endDrawingPeriod();
            }
        }, 1000);
    }

    startLeaderboardTimer() {
        let leaderboardTimer = 6;

        let counter = setInterval(() => {
            leaderboardTimer--;
            this.emit('leaderboard_timer_tick', leaderboardTimer, this);

            // Stop the timer on 0
            if (this.leaderboardTimer == 0) {
                clearInterval(counter);
                this.setGameStatus(GameStatus.Choosing);
                this.emit('round_finished', this);
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

    resetRound() {
        this.roundTimestamp = 60;
        this.word = null;
        this.wordDifficultyPoints = null;
        this.guessedCorrectly = [];
    }

    getCurrentRoundNumber() {
        return this.drawingPlayer + 1;
    }
}

// Enum for game status
export const GameStatus = {
    WAITING: "WAITING",
    PLAYING: "PLAYING",
    FINISHED: "FINISHED",
    CHOOSING: "CHOOSING",
    LEADERBOARD: "LEADERBOARD"
};

export const WordDifficultyPoints = {
    EASY: 5,
    MEDIUM: 15,
    HARD: 25
}
