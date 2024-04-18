import { Guess } from '../Models/Guess.mjs';

describe('Guess', () => {
  let guess;

  beforeEach(() => {
    guess = new Guess('testGuess', false);
  });

  it('should set the isCorrect status', () => {
    guess.setIsCorrect(true);
    expect(guess.isCorrect).toBe(true);
  });
  
});