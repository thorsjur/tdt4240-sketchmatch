export class GuessWord {
    constructor(id, word, difficulty) {
        this.id = id;
        this.word = word;
        this.difficulty = difficulty;
    }

    // Get masked word
    getMaskedWord() {
        return this.word.replace(/./g, '_ ');
    }
}