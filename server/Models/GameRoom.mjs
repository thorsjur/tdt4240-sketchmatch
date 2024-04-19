import { Round } from "./Round.mjs";

export class GameRoom{
    constructor(id, gameCode, gameName, gameCapacity = 2) {
        this.id = id;
        this.gameCode = gameCode;
        this.gameName = gameName;
        this.gameCapacity = gameCapacity;
        this.players = [];
        this.gameStatus = GameStatus.Waiting;
   

        this.rounds = []; // Number of rounds match number of players
        this.currentRound = 1; // Current round being played (eg. 1 would be the first round in the game)
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

    // Get rounds
    getRounds() {
        return this.rounds;
    }

    // Get roomId
    getRoomId() {
        return this.id;
    }

    // Update status of the game
    updateStatus() {
        if ((this.gameStatus == GameStatus.Waiting) && (this.rounds[this.currentRound - 1].getTimer() == 60)) {
            this.gameStatus = GameStatus.Playing;
        } else if (this.rounds[this.currentRound - 1].getTimer() <= 0) {
            // If the timer has run out, move to the next round
            if (this.currentRound  + 1 > this.rounds.length) {
                this.gameStatus = GameStatus.Finished;
            }
            else{
                this.currentRound++;
            }
        }
    }

    // Add round
    addRound(round) {
        this.rounds.push(round);
    }

    // Initialize rounds
    initializeRounds() {
        this.players.forEach((player, index) => {
            const round = new Round(index + 1, this.id, player);
            round.setDrawingPlayer(player);
            this.addRound(round);
        });
    }

    getGameCode() {
        return this.gameCode;
    }
}

// Enum for game status
export const GameStatus = {
    Waiting: "Waiting",
    Playing: "Playing",
    Finished: "Finished",
};

