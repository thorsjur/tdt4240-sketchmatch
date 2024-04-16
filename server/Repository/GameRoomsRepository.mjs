import crypto from "crypto";
import { GameRoom } from '../Models/GameRoom.mjs';
import { addGameRoom, getAllGameRooms, getGameRoomByCode, getGameRoomById, removeGameRoom } from '../db/rxdbSetup.mjs';

export class GameRoomsRepository {
    constructor() {
        if (!GameRoomsRepository.instance) {
            GameRoomsRepository.instance = this;
        }

        return GameRoomsRepository.instance;
    }

    // Get all game rooms
    getGameRooms() {
        return getAllGameRooms();
    }

    // Create a new game room
    createGameRoom(name, player, capacity) {
        // Generate a random ID for the game room
        var id = crypto.randomUUID();

        // Generate a HEX code for the game room to be used as a "enter by code" feature
        var gameCode = Math.random().toString(16).substr(2, 6);

        let gameRoom = new GameRoom(id, gameCode, name, capacity);
        gameRoom.addPlayer(player);

        addGameRoom(gameRoom);

        return gameRoom;
    }

    // Get game room by ID
    getGameRoomById(gameRoomId) {
        return getGameRoomById(gameRoomId);
    }

    // Get game room by code
    getGameRoomByCode(gameCode) {
        return getGameRoomByCode(gameCode);
    }

    // Remove game room by ID
    removeGameRoomById(gameRoomId) {
        removeGameRoom(gameRoomId);
    }

}