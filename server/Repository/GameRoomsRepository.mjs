import { EventEmitter } from "events";
import { GameRoom, GameStatus, WordDifficultyPoints } from "../Models/GameRoom.mjs";

export class GameRoomsRepository extends EventEmitter{
    constructor() {
        super();
        if (!GameRoomsRepository.instance) {
            this.gameRooms = [];
            GameRoomsRepository.instance = this;
        }

        return GameRoomsRepository.instance;
    }

    // Get all game rooms
    getGameRooms() {
        return this.gameRooms.filter(gameRoom => gameRoom.gameStatus == GameStatus.WAITING);
    }

    // Set the draw word for the game room
    updateDrawWord(drawWord, currentRound) {
        currentRound.setDrawWord(drawWord);
    }

    updateDifficulty(difficulty, currentRound) {
        currentRound.setDifficulty(difficulty);
    }

    // Create a new game room
    createGameRoom(name, player, capacity) {
        // Generate a random ID for the game room
        var id = Math.floor(Math.random() * 1000000);

        // Generate a HEX code for the game room to be used as a "enter by code" feature
        var gameCode = Math.random().toString(16).substr(2, 6);

        let gameRoom = new GameRoom(id, gameCode, name, capacity);
        gameRoom.addPlayer(player);
        this.gameRooms.push(gameRoom);

        gameRoom.on('round_has_been_created', gameRoom => {
            this.emit('round_has_been_created', gameRoom);
        });

        gameRoom.on('round_has_started', gameRoom => {
            this.emit('round_has_started', gameRoom);
        });

        gameRoom.on('open_leaderboard', gameRoom => {
            this.emit('open_leaderboard', gameRoom);
        });

        gameRoom.on('round_timer_tick', (gameRoom) => {
            this.emit('round_timer_tick', gameRoom);
        });

        gameRoom.on('leaderboard_timer_tick', (leaderboardTimer, gameRoom) => {
            this.emit('leaderboard_timer_tick', leaderboardTimer, gameRoom);
        });

        gameRoom.on('round_finished', gameRoom => {
            this.emit('round_finished', gameRoom);
            if (gameRoom.gameStatus == GameStatus.FINISHED) {
                this.removeGameRoomById(gameRoom.id);
            }
        });

        gameRoom.on('answer_to_guess', (playerId, isCorrect, gameRoom) => {
            this.emit('answer_to_guess', playerId, isCorrect, gameRoom);
        });

        return gameRoom;
    }

    // Get game room by ID
    getGameRoomById(id) {
        return this.gameRooms.find((gameRoom) => gameRoom.id === id);
    }

    // Get game room by code
    getGameRoomByCode(code) {
        return this.gameRooms.find((gameRoom) => gameRoom.gameCode === code);
    }

    // Remove game room by ID
    removeGameRoomById(id) {
        this.gameRooms = this.gameRooms.filter(
            (gameRoom) => gameRoom.id !== id
        );
    }

    // Remove player from game room
    removePlayerFromGameRoom(hwid, roomId) {
        const gameRoom = this.getGameRoomById(roomId);
        gameRoom?.removePlayerByHwid(hwid);
    }

    startRound(gameRoomId, newWord, newWordDifficultyPoints) {
        const gameRoom = this.gameRooms.find((gameRoom) => gameRoom.id === gameRoomId);
        gameRoom.initializeRound(newWord, newWordDifficultyPoints);
    }

    handleGuess(gameRoomId, playerId, guessedWord) {
        const gameRoom = this.gameRooms.find((gameRoom) => gameRoom.id === gameRoomId);
        gameRoom.handleGuess(playerId, guessedWord);
    }

    handlePlayerLeaving(playerId) {
        const gameRoom = this.gameRooms.find((gameRoom) => gameRoom.players.some((player) => player.id === playerId));
    }

    getDificultyObjByString(difficulty) {
        switch (difficulty.toLowerCase()) {
            case "easy":
                return WordDifficultyPoints.EASY;
            case "medium":
                return WordDifficultyPoints.MEDIUM;
            case "hard":
                return WordDifficultyPoints.HARD;
            default:
                return WordDifficultyPoints.EASY;
        }
    }
}
