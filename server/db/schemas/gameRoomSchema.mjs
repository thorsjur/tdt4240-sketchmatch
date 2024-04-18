export const gameRoomSchema = {
    title: 'gameRoomSchema',
    version: 0,
    description: 'Holds the game rooms',
    primaryKey: 'id',
    type: 'object',
    properties: {
        id: {
            type: 'string',
            maxLength: 100,
        },
        gameCode: {
            type: 'string'
        },
        gameName: {
            type: 'string'
        },
        gameCapacity: {
            type: 'number',
            minimum: 0,
            maximum: 5,
            multipleOf: 1
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
        'id',
        'gameCode',
        'gameName',
        'gameCapacity',
        'players',
        'gameStatus'
    ]
}