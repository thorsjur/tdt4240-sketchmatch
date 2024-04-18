import { Player } from '../Models/Player.mjs';

describe('Player', () => {
  let player;

  beforeEach(() => {
    player = new Player(1, 'hwid1', 'player1');
  });

  it('should set the nickname', () => {
    player.setNickname('newNickname');
    expect(player.nickname).toBe('newNickname');
  });

  it('should set the score', () => {
    player.setScore(10);
    expect(player.score).toBe(10);
  });

  it('should get the score', () => {
    player.setScore(20);
    expect(player.getScore()).toBe(20);
  });

  it('should get the isDrawing status', () => {
    expect(player.getIsDrawing()).toBe(false);
  });

  it('should set the isDrawing status', () => {
    player.setIsDrawing(true);
    expect(player.getIsDrawing()).toBe(true);
  });
});