import { addRxPlugin, createRxDatabase } from "rxdb";
import { getRxStorageMemory } from 'rxdb/plugins/storage-memory';

import { gameRoomSchema } from './schemas/gameRoomSchema.mjs';
import { guessesSchema } from "./schemas/guessesSchema.mjs";
import { playerSchema } from './schemas/playerSchema.mjs';

// Plugin for dev mode, not to be used in production
import { RxDBDevModePlugin } from 'rxdb/plugins/dev-mode';
addRxPlugin(RxDBDevModePlugin);

// Plugin needed if schema versions needs to be bumped,
// in which case, migrationStrategy must be specified in each collection
import { RxDBMigrationPlugin } from 'rxdb/plugins/migration-schema';
addRxPlugin(RxDBMigrationPlugin);


export const database = await createRxDatabase({
    name: 'sketchmatchdb',
    storage: getRxStorageMemory(),
    multiInstance: false,
    eventReduce: true,
    ignoreDuplicate: true,
});

const collections = await database.addCollections({
    guesses: {
        schema: guessesSchema,
        autoMigrate: true,
    },
    players: {
        schema: playerSchema,
        autoMigrate: true,
    },
    gamerooms: {
        schema: gameRoomSchema,
        autoMigrate: true,
    } 
});

export const addPlayerToDB = (player) => {
    collections.players.insert({
        id: player.id,
        hwid: player.hwid,
        nickname: player.nickname,
        score: player.score
    });
}

export const getPlayerById = async (playerId) => {
    const player = await collections.players.findOne(playerId).exec();
    return player;
}


export const getPlayerByHWID = async (hwid) => {
    const player = await collections.players.findOne({
        selector: {
            hwid: hwid
        }
    }).exec();
    return player;
}

export const removePlayer = async (player) => {
    const doc = await collections.players.findOne(player.id).exec();
    doc.remove();
}

export const removePlayerByHWID = async (hwid) => {
    const query = collections.players.findOne({
        selector: {
            hwid: hwid
        }
    }).exec();
    await query.remove();
}

export const incrementScore = async (player, pointsEarned) => {
    const doc = await collections.players.findOne(player.id).exec();
    await doc.incrementalUpdate({
        $inc: {
            score: pointsEarned
        }
    });
}

export const addGameRoom = (gameRoom) => {
    const gameRooms = collections.gamerooms;

    gameRooms.insert({
        id: gameRoom.id,
        gameCode: gameRoom.gameCode,
        gameName: gameRoom.gameName,
        gameCapacity: gameRoom.gameCapacity,
        players: gameRoom.players,
        GameStatus: gameRoom.gameStatus
    });
}

export const getAllGameRooms = async () => {
    const query = collections.gamerooms.find();
    const results = await query.exec();
    return results;
}

export const getGameRoomById = async (gameRoomId) => {
    const gameRoom = await collections.gamerooms.findOne(gameRoomId).exec();
    return gameRoom;
}

export const getGameRoomByCode = async (gameRoomCode) => {
    const gameRoom = await collections.gamerooms.findOne({
        selector: {
            gameCode: gameRoomCode
        }
    }).exec();
    return gameRoom;
}

export const removeGameRoom = async (id) => {
    const doc = await collections.gamerooms.findOne(id).exec();
    doc.remove();
}

export const addPlayerToGameRoom = async (gameRoom, player) => {
    const doc = await collections.gamerooms.findOne(gameRoom.id).exec();
    const players = doc.get('players');
    const newPlayers = players.push(player)

    await doc.update({
        $set: {
            players: newPlayers
        }
    });
}

export const removePlayerFromGameRoom = async (gameRoom, player) => {
    const doc = await collections.gamerooms.findOne(gameRoom.id).exec();
    const players = doc.get('players');
    const newPlayers = players.filter((p) => p.id !== player.id);

    await doc.update({
        $set: {
            players: newPlayers
        }
    });
}

export const updateGameRoomStatus = async (gameRoom, newStatus) => {
    const doc = await collections.gamerooms.findOne(gameRoom.id).exec();

    await doc.update({
        $set: {
            gameStatus: newStatus
        }
    });
}