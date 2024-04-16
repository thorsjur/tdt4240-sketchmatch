/**
 * This schema 
 */

export const guessesSchema = {
    title: 'guessesSchema',
    version: 0,
    description: 'Holds the possible guesses and points given for them',
    primaryKey: 'id',
    type: 'object',
    properties: {
        id: {
            type: 'string',
            maxLength: 100,
        },
        guess: {
            type: 'string',
        },
        points: {
            type: 'number',
            minimum: 0,
            maximum: 100,
            mulitpleOf: 1
        }
    },
    required: [
        'id',
        'guess',
        'points'
    ]
};