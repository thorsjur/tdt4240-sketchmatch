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
            type: 'number',
            maxLength: 100 // <- the primary key must have set maxLength
        },
        guess: {
            type: 'string',
        },
        points: {
            type: 'number'
        }
    },
    required: [
        id,
        guess,
        points
    ]
}