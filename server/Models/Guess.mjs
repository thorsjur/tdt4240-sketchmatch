export class Guess {
    constructor(inputGuess, isCorrect) {
        this.inputGuess = inputGuess;
        this.isCorrect = isCorrect;
    }

    // Set isCorrect
    setIsCorrect(isCorrect) {
        this.isCorrect = isCorrect;
    }
}
