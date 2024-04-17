import { Player } from '../Models/Player.mjs';

export class PlayersRepository {
    constructor() {
        if (!PlayersRepository.instance) {
            this.players = [];
            PlayersRepository.instance = this;
        }

        return PlayersRepository.instance;
    }

    addPlayer(hwid, nickname) {
        // Generate a random ID for the player
        var id = Math.floor(Math.random() * 1000000);

        var player = new Player(id, hwid, nickname);

        this.players.push(player);
        return player;
    }

    // Get player by HWID
    getPlayerByHWID(hwid) {
        return this.players.find(player => player.hwid === hwid);
    }

    // Remove player by HWID
    removePlayerByHWID(hwid) {
        this.players = this.players.filter(player => player.hwid !== hwid);
    }
}