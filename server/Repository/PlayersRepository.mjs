import crypto from "crypto";
import { Player } from '../Models/Player.mjs';
import { addPlayerToDB, getPlayerByHWID, removePlayerByHWID } from '../db/rxdbSetup.mjs';

export class PlayersRepository {
    constructor() {
        if (!PlayersRepository.instance) {
            PlayersRepository.instance = this;
        }

        return PlayersRepository.instance;
    }

    addPlayer(hwid, nickname) {
        // Generate a random ID for the player
        var id = crypto.randomUUID();

        var player = new Player(id, hwid, nickname);

        addPlayerToDB(player)
        return player;
    }

    // Get player by HWID
    getPlayerByHWID(hwid) {
        return getPlayerByHWID(hwid);
    }

    // Remove player by HWID
    removePlayerByHWID(hwid) {
        removePlayerByHWID(hwid);
    }
}