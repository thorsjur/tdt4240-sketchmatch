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
    try {    
        collections.players.insert({
            id: player.id,
            hwid: player.hwid,
            nickname: player.nickname,
            score: player.score
        });
    } catch (error) {
        console.error(error);
    }
}

export const getPlayerById = async (playerId) => {
    try {  
        const player = await collections.players.findOne(playerId).exec();
        return player;
    } catch (error) {
        console.error(error);
    }
}


export const getPlayerByHWID = async (hwid) => {
    try {  
        const player = await collections.players.findOne({
            selector: {
                hwid: hwid
            }
        }).exec();
        return player._data;
    } catch (error) {
        console.error(error);
    }
}

export const removePlayer = async (player) => {
    try { 
        const doc = await collections.players.findOne(player.id).exec();
        doc.remove();
    } catch (error) {
        console.error(error);
    }
}

export const removePlayerByHWID = async (hwid) => {
    try {  
        const query = collections.players.findOne({
            selector: {
                hwid: hwid
            }
        }).exec();
        await query.remove();
    } catch (error) {
        console.error(error);
    }
}

export const incrementScore = async (player, pointsEarned) => {
    try {
        const doc = await collections.players.findOne(player.id).exec();
        await doc.incrementalUpdate({
            $inc: {
                score: pointsEarned
            }
        });
    } catch (error) {
        console.error(error);
    }
}

export const addGameRoom = (gameRoom) => {
    try {
        const gameRooms = collections.gamerooms;
    
        gameRooms.insert({
            id: gameRoom.id,
            gameCode: gameRoom.gameCode,
            gameName: gameRoom.gameName,
            gameCapacity: gameRoom.gameCapacity,
            players: gameRoom.players,
            GameStatus: gameRoom.gameStatus
        });
    } catch (error) {
        console.error(error);
    }
}

export const getAllGameRooms = async () => {
    try { 
        const query = collections.gamerooms.find();
        const gameRooms = await query.exec();
        return gameRooms;
    } catch (error) {
        console.error(error);
    }
}

export const getGameRoomById = async (gameRoomId) => {
    try {
        const gameRoom = await collections.gamerooms.findOne(gameRoomId).exec();
        return gameRoom;
    } catch (error) {
        console.error(error);
    }
}

export const getGameRoomByCode = async (gameRoomCode) => {
    try {
        const gameRoom = await collections.gamerooms.findOne({
            selector: {
                gameCode: gameRoomCode
            }
        }).exec();
        return gameRoom;
    } catch (error) {
        console.error(error);
    }
}

export const removeGameRoom = async (id) => {
    try {
        const doc = await collections.gamerooms.findOne(id).exec();
        doc.remove();
    } catch (error) {
        console.error(error);
    }
}

export const addPlayerToGameRoom = async (gameRoomId, player) => {
    try {
        const doc = await collections.gamerooms.findOne(gameRoomId).exec();
        const players = doc.get('players');
        const newPlayers = players.push(player)
    
        await doc.update({
            $set: {
                players: newPlayers
            }
        });
    } catch (error) {
        console.error(error);
    }
}

export const removePlayerFromGameRoom = async (gameRoom, player) => {
    try {        
        const doc = await collections.gamerooms.findOne(gameRoom.id).exec();
        const players = doc.get('players');
        const newPlayers = players.filter((p) => p.id !== player.id);
    
        await doc.update({
            $set: {
                players: newPlayers
            }
        });
    } catch (error) {
        console.error(error);
    }
}

export const updateGameRoomStatus = async (gameRoom, newStatus) => {
    try {
        const doc = await collections.gamerooms.findOne(gameRoom.id).exec();
    
        await doc.update({
            $set: {
                gameStatus: newStatus
            }
        });
    } catch (error) {
        console.error(error);
    }
}