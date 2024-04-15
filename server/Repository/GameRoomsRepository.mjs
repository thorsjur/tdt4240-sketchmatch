import { Player } from '../Models/Player.mjs';
import { GameRoom } from '../Models/GameRoom.mjs';

export class GameRoomsRepository {
    constructor() {
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

    // Create a new game room
    createGameRoom(name, player, capacity) {
        // Generate a random ID for the game room
        var id = Math.floor(Math.random() * 1000000);

        // Generate a HEX code for the game room to be used as a "enter by code" feature
        var gameCode = Math.random().toString(16).substr(2, 6);

        let gameRoom = new GameRoom(id, gameCode, name, capacity);

        this.gameRooms.push(gameRoom);

        return gameRoom;
    }

    // Get game room by ID
    getGameRoomById(id) {
        return this.gameRooms.find(gameRoom => gameRoom.id === id);
    }

    // Get game room by code
    getGameRoomByCode(code) {
        return this.gameRooms.find(gameRoom => gameRoom.gameCode === code);
    }

    // Remove game room by ID
    removeGameRoomById(id) {
        this.gameRooms = this.gameRooms.filter(gameRoom => gameRoom.id !== id);
    }

}