export const gameRoomSchema = {
    title: 'gameRoomSchema',
    version: 1,
    description: 'Holds the game rooms',
    primaryKey: 'id',
    type: 'object',
    properties: {
        id: {
            type: 'number',
            maxLength: 15
        },
        gameCode: {
            type: 'string'
        },
        gameName: {
            type: 'string'
        },
        gameCapacity: {
            type: 'number'
        },
        players: {
            type: 'array',
            ref: 'player',
            items: {
                type: 'string'
            }
        },
        gameStatus: {
            type: 'string',
            enum: ['Waiting', 'Playing', 'Finished']
        }
    },
    required: [
        id,
        gameCode,
        gameName,
        gameCapacity,
        players,
        gameStatus
    ]
}