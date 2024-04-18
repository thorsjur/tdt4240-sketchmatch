import { Guess } from "../Models/Guess.mjs";
import { GameRoom } from "../Models/GameRoom.mjs";
import { EventEmitter } from "events";

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
        return this.gameRooms;
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
        // var id = Math.floor(Math.random() * 1000000);
        var id = 1;

        // Generate a HEX code for the game room to be used as a "enter by code" feature
        var gameCode = Math.random().toString(16).substr(2, 6);

        let gameRoom = new GameRoom(id, gameCode, name, capacity);
        gameRoom.addPlayer(player);
        gameRoom.initializeRounds();

        this.gameRooms.push(gameRoom);

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


    // Get roomId from gameCode
    getRoomIdFromGameCode(code) {
        return this.gameRooms.find((gameRoom) => gameRoom.gameCode === code).id;
    }


    // Remove game room by ID
    removeGameRoomById(id) {
        this.gameRooms = this.gameRooms.filter(
            (gameRoom) => gameRoom.id !== id
        );
    }

    // Start the round
    startRound(roomId) {
        const gameRoom = this.gameRooms.find((gameRoom) => gameRoom.id === roomId);

        console.log()
        if(!gameRoom) {
            console.log(`Game room with ID ${roomId} not found`);
            return;
        }
        const round = this.findCurrentRound(gameRoom);

        round.startRound(gameRoom);

        round.on('timer_tick', (time, gameRoom) => {
            this.emit('timer_tick', time, gameRoom);
        });

        round.on('round_finished', (gameRoom) => {
            this.emit('round_finished', gameRoom);
        });
    }


    // Check if the guess is correct
    checkGuess(guess, round, timestamp, guessingPlayer, drawingPlayer) {
        const isCorrect = round.drawWord === guess;

        if (isCorrect) {
            this.givePoints(round, timestamp, guessingPlayer, drawingPlayer);
        }
        return new Guess(guess, isCorrect);
    }

    givePoints(round, timestamp, guessingPlayer, drawingPlayer) {
        //Give guesser points
        const guessingScore = round.calculateGuesserScore(timestamp);
        guessingPlayer.setScore(guessingPlayer.getScore() + guessingScore);
        // Give drawer points
        const drawingScore = round.calculateDrawerScore();
        if (drawingPlayer) {
            drawingPlayer.setScore(drawingPlayer.getScore() + drawingScore);
        } else {
            console.log("No drawing player found");
        }

        // TODO: Update points in database (maybe do elsewhere) (will send points when round is over)
    }

    // Find current round
    findCurrentRound(gameRoom) {
        const rounds = gameRoom.getRounds();
        const currentRound = rounds[gameRoom.currentRound - 1];
        return currentRound;
    }
}
