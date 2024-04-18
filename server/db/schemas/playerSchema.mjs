export const playerSchema = {
    title: 'playerSchema',
    version: 0,
    description: 'Holds the players',
    primaryKey: 'id',
    type: 'object',
    properties: {
        id: {
            type: 'string',
            maxLength: 100,
        },
        hwid: {
            type: 'string'
        },
        nickname: {
            type: 'string'
        },
        score: {
            type: 'number',
            minimum: 0,
            maximum: 2500,
            multipleOf: 1
        }
    },
    required: [
        'id',
        'hwid',
        'nickname',
        'score'
    ]
}