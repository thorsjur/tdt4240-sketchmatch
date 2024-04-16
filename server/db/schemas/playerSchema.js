export const playerSchema = {
    title: 'playerSchema',
    version: 1,
    description: 'Holds the players',
    primaryKey: 'id',
    type: 'object',
    properties: {
        id: {
            type: 'number',
            maxLength: 15
        },
        hwid: {
            type: 'string'
        },
        nickname: {
            type: 'string'
        },
        score: {
            type: 'number',
            minimum: 0
        }
    },
    required: [
        id,
        hwid,
        nickname,
        score
    ]
}