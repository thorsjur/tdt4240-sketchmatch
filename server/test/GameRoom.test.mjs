import { GameRoom, GameStatus } from '../Models/GameRoom.mjs';
import { Round } from '../Models/Round.mjs';
import { Player } from '../Models/Player.mjs';

describe('GameRoom', () => {
  let gameRoom;

  beforeEach(() => {
    gameRoom = new GameRoom(1, 'gameCode', 'gameName');
  });

  it('should add a player to the game room', () => {
    const player = { id: 1 };
    gameRoom.addPlayer(player);

    expect(gameRoom.players).toContain(player);
  });

  it('should remove a player from the game room', () => {
    const player1 = { id: 1 };
    const player2 = { id: 2 };

    gameRoom.addPlayer(player1);
    gameRoom.addPlayer(player2);
    gameRoom.removePlayer(player1);

    expect(gameRoom.players).not.toContain(player1);
    expect(gameRoom.players).toContain(player2);
  });

  it('should change the game status', () => {
    gameRoom.changeGameStatus(GameStatus.Playing);
    expect(gameRoom.gameStatus).toBe(GameStatus.Playing);
  });

  it('should get the rounds', () => {
    const round1 = new Round(1, gameRoom.id, 'player1');
    const round2 = new Round(2, gameRoom.id, 'player2');
    gameRoom.addRound(round1);
    gameRoom.addRound(round2);
    expect(gameRoom.getRounds()).toEqual([round1, round2]);
  });

  it('should get the room ID', () => {
    expect(gameRoom.getRoomId()).toBe(1);
  });

  it('should update the game status', () => {
    const round = new Round(1, gameRoom.id, 'player');

    gameRoom.addRound(round);

    round.setTimer(60);
    gameRoom.updateStatus();
    expect(gameRoom.gameStatus).toBe(GameStatus.Playing);

    round.setTimer(0);
    gameRoom.updateStatus();
    expect(gameRoom.gameStatus).toBe(GameStatus.Finished);
  });

  it('should add a round to the game room', () => {
    const round = new Round(1, gameRoom.id, 'player');

    gameRoom.addRound(round);
    expect(gameRoom.rounds).toContain(round);
  });


  it('should initialize rounds with correct parameters', () => {
    const player1 = new Player(1, 'hwid1', 'player1');
    const player2 = new Player(2, 'hwid2', 'player2');

    gameRoom.addPlayer(player1);
    gameRoom.addPlayer(player2);
    gameRoom.initializeRounds();

    expect(gameRoom.rounds.length).toBe(2);

    const round1 = gameRoom.rounds[0];
    const round2 = gameRoom.rounds[1];

    expect(round1.getSequenceNumber()).toBe(1);
    expect(round1.getGameRoomId()).toBe(gameRoom.getRoomId());
    expect(round1.drawingPlayer).toBe(player1);

    expect(round2.getSequenceNumber()).toBe(2);
    expect(round2.getGameRoomId()).toBe(gameRoom.getRoomId());
    expect(round2.drawingPlayer).toBe(player2);
  });

  it('should increment the current round when the timer runs out', () => {
    const round1 = new Round(1, gameRoom.id, 'player1');
    const round2 = new Round(2, gameRoom.id, 'player2');
    gameRoom.addRound(round1);
    gameRoom.addRound(round2);
  
    round1.setTimer(0);
    gameRoom.updateStatus();

    expect(gameRoom.currentRound).toBe(2);
  });

});

