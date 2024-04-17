import { Player } from './Player.mjs';

export class GameRoom {
    constructor(id, gameCode, gameName, gameCapacity = 2) {
        this.id = id;
        this.gameCode = gameCode;
        this.gameName = gameName;
        this.gameCapacity = gameCapacity;
        this.players = [];
        this.gameStatus = GameStatus.Waiting;
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

    // Add more methods as needed
}

// Enum for game status
export const GameStatus = {
    Waiting: "Waiting",
    Playing: "Playing",
    Finished: "Finished",
};