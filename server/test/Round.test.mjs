import { Round } from '../Models/Round.mjs';

describe('Round', () => {
    let round;

    beforeEach(() => {
        round = new Round(1, 'gameRoomId', 'drawingPlayer');
    });

    it('should return the correct sequence number', () => {
        expect(round.getSequenceNumber()).toBe(1);
    });

    it('should return the correct drawing player', () => {
        expect(round.getDrawingPlayer()).toBe('drawingPlayer');
    });

    it('should return the correct draw word', () => {
        round.setDrawWord('unicorn');
        expect(round.getDrawWord()).toBe('unicorn');
    });

    it('should return the correct difficulty', () => {
        round.setDifficulty('MEDIUM');
        expect(round.getDifficulty()).toBe('MEDIUM');
    });

    it('should return the correct timer', () => {
        round.setTimer(30);
        expect(round.getTimer()).toBe(30);
    });

    it('should calculate the guesser score correctly', () => {
        expect(round.calculateGuesserScore(50)).toBe(100);
        expect(round.calculateGuesserScore(35)).toBe(75);
        expect(round.calculateGuesserScore(20)).toBe(50);
        expect(round.calculateGuesserScore(5)).toBe(25);
        expect(round.calculateGuesserScore(0)).toBe(0);
    });

    it('should calculate the drawer score correctly', () => {
        round.setDifficulty('easy');
        expect(round.calculateDrawerScore()).toBe(5);

        round.setDifficulty('medium');
        expect(round.calculateDrawerScore()).toBe(15);

        round.setDifficulty('hard');
        expect(round.calculateDrawerScore()).toBe(25);
    });
});
