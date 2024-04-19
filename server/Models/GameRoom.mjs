import { EventEmitter } from "events";
import { Player } from "./Player.mjs";

export const GameRoomSettings = {
    ROUND_DURATION: 30,
    LEADERBOARD_DURATION: 6,
    ROUND_CREATE_DELAY: 2000 // wait x ms before creating a new round after the game is full (just to make sure the last player has been navigated to the lobby screen)
}

export class GameRoom extends EventEmitter {

    constructor(id, gameCode, gameName, gameCapacity = 2) {
        super()
        this.id = id;
        this.gameCode = gameCode;
        this.gameName = gameName;
        this.gameCapacity = gameCapacity;
        this.players = [];
        this.drawingPlayer = 0;
        this.gameStatus = GameStatus.Waiting;
        this.roundTimestamp = GameRoomSettings.ROUND_DURATION;
        this.word = null;
        this.wordDifficultyPoints = null;
        this.guessedCorrectly = [];
    }

    addPlayer(player) {
        this.players.push(player);

        console.log(`Player ${player.id} has joined the game room ${this.id}`);

        if(this.players.length === this.gameCapacity) {
            this.setGameStatus(GameStatus.CHOOSING);
            // Emit after 300 ms
            setTimeout(() => {
                this.emit('round_has_been_created', this);
            }, GameRoomSettings.ROUND_CREATE_DELAY);
        }
    }

    removePlayer(player) {
        this.players = this.players.filter((p) => p.id !== player.id);
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
        
        this.setGameStatus(GameStatus.PLAYING);
        this.emit('round_has_started', this);
        this.startRoundTimer();
    }

    endDrawingPeriod() {
        this.drawingPlayer++;
        if (this.drawingPlayer + 1 > this.players.length) {
            this.endGame()
            return;
        }
        this.setGameStatus(GameStatus.LEADERBOARD);
        this.emit('open_leaderboard', this);
        this.startLeaderboardTimer();
    }

    handleGuess(playerId, guessedWord) {
        var player = this.players.find((p) => p.id === playerId);
        //let drawer = this.players.get(this.drawingPlayer);
        let isCorrect = guessedWord === this.word;

        if (isCorrect) {
            player.incrementScore(this.calculateGuesserScore(this.roundTimestamp));
            this.guessedCorrectly.push(playerId);
            //drawer.incrementScore(this.wordDifficultyPoints);
        }

        this.emit('answer_to_guess', playerId, isCorrect, this);
    }

    startRoundTimer() {
        let counter = setInterval(() => {
            this.roundTimestamp--;
            this.emit('round_timer_tick', this);
          
            // Stop the timer on 0
            if (this.roundTimestamp == 0) {
                clearInterval(counter);
                this.endDrawingPeriod();
            }
        }, 1000);
    }

    startLeaderboardTimer() {
        let leaderboardTimer = GameRoomSettings.LEADERBOARD_DURATION;

        let counter = setInterval(() => {
            leaderboardTimer--;
            this.emit('leaderboard_timer_tick', leaderboardTimer, this);

            // Stop the timer on 0
            if (leaderboardTimer == 0) {
                clearInterval(counter);
                this.setGameStatus(GameStatus.CHOOSING);
                this.emit('round_finished', this);
            }
        }, 1000);
    }

    endGame() {
        this.setGameStatus(GameStatus.FINISHED);
        this.emit('round_finished', this);
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
        this.roundTimestamp = GameRoomSettings.ROUND_DURATION;
        this.word = null;
        this.wordDifficultyPoints = null;
        this.guessedCorrectly = [];
    }

    getCurrentRoundNumber() {
        return this.drawingPlayer + 1;
    }

    // Serialize the game room
    serialize() {
        return {
            id: this.id,
            gameCode: this.gameCode,
            gameName: this.gameName,
            gameCapacity: this.gameCapacity,
            players: this.players,
            gameStatus: this.gameStatus,
            drawingPlayer: this.drawingPlayer,
            roundTimestamp: this.roundTimestamp,
            word: this.word,
            wordDifficultyPoints: this.wordDifficultyPoints,
            guessedCorrectly: this.guessedCorrectly
        }
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