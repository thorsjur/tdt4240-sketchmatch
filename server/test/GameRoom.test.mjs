import { GameRoom, GameStatus, GameRoomSettings } from '../Models/GameRoom.mjs';
import { Player } from '../Models/Player.mjs';

describe('GameRoom', () => {
  let gameRoom;

  beforeEach(() => {
    gameRoom = new GameRoom(1, 'ABC123', 'Test Game', 2);
  });

  it('should add a player to the game room', () => {
    const player = new Player(1, 'hwid1', 'player1');
    gameRoom.addPlayer(player);

    expect(gameRoom.players.length).toBe(1);
  });

  it('should remove a player from the game room', () => {
    const player1 = new Player(1, 'hwid1', 'player1');
    const player2 = new Player(2, 'hwid2', 'player2');

    gameRoom.addPlayer(player1);
    gameRoom.addPlayer(player2);
    gameRoom.removePlayer(player1);

    expect(gameRoom.players.length).toBe(1);
    expect(gameRoom.players[0]).toBe(player2);
  });

  it('should initialize a round', () => {
    const newWord = 'sun';
    const newWordDifficultyPoints = 5;
    const player1 = new Player(1, 'hwid1', 'player1');
    const player2 = new Player(2, 'hwid2', 'player2');

    gameRoom.addPlayer(player1);
    gameRoom.addPlayer(player2);

    gameRoom.initializeRound(newWord, newWordDifficultyPoints);

    expect(gameRoom.word).toBe(newWord);
    expect(gameRoom.wordDifficultyPoints).toBe(newWordDifficultyPoints);
    expect(gameRoom.gameStatus).toBe(GameStatus.PLAYING);
    expect(gameRoom.drawingPlayer).toBe(0);
  });

  it('should end the drawing period', () => {
    const player1 = new Player(1, 'hwid1', 'player1');
    const player2 = new Player(2, 'hwid2', 'player2');

    gameRoom.addPlayer(player1);
    gameRoom.addPlayer(player2);
    gameRoom.endDrawingPeriod();

    expect(gameRoom.getGameStatus()).toBe(GameStatus.LEADERBOARD);
  });

  it('should handle a guess', () => {
    const player1 = new Player(1, 'hwid1', 'player1');
    const player2 = new Player(2, 'hwid2', 'player2');

    gameRoom.addPlayer(player1);
    gameRoom.addPlayer(player2);
    gameRoom.initializeRound('sun', 5);
    gameRoom.handleGuess(player1.id, 'sun');

    expect(player1.getScore()).toBe(105);
    expect(player2.getScore()).toBe(0);
    expect(gameRoom.getGuessedCorrectly()).toContain(player1.id);
  });


  it('should end the game', () => {
    const player1 = new Player(1, 'hwid1', 'player1');
    const player2 = new Player(2, 'hwid2', 'player2');
    gameRoom.addPlayer(player1);
    gameRoom.addPlayer(player2);
    gameRoom.endGame();

    expect(gameRoom.getGameStatus()).toBe(GameStatus.FINISHED);
    expect(gameRoom.getPlayers()[0].getScore()).toBe(0);
    expect(gameRoom.getPlayers()[1].getScore()).toBe(0);
  });

  it('should reset the round', () => {
    gameRoom.resetRound();

    expect(gameRoom.roundTimestamp).toBe(GameRoomSettings.ROUND_DURATION);
    expect(gameRoom.word).toBeNull();
    expect(gameRoom.wordDifficultyPoints).toBeNull();
    expect(gameRoom.getGuessedCorrectly()).toEqual([]);
    expect(gameRoom.stopTimer).toBe(false);
  });

  it('should get the current round number', () => {
    gameRoom.drawingPlayer = 1;
    expect(gameRoom.getCurrentRoundNumber()).toBe(2);
  });

  it('should serialize the game room', () => {
    const serializedGameRoom = gameRoom.serialize();
    expect(serializedGameRoom.id).toBe(gameRoom.id);
    expect(serializedGameRoom.gameCode).toBe(gameRoom.gameCode);
    expect(serializedGameRoom.gameName).toBe(gameRoom.gameName);
    expect(serializedGameRoom.gameCapacity).toBe(gameRoom.gameCapacity);
    expect(serializedGameRoom.players).toBe(gameRoom.players);
    expect(serializedGameRoom.gameStatus).toBe(gameRoom.gameStatus);
    expect(serializedGameRoom.drawingPlayer).toBe(gameRoom.drawingPlayer);
    expect(serializedGameRoom.roundTimestamp).toBe(gameRoom.roundTimestamp);
    expect(serializedGameRoom.word).toBe(gameRoom.word);
    expect(serializedGameRoom.wordDifficultyPoints).toBe(gameRoom.wordDifficultyPoints);
    expect(serializedGameRoom.guessedCorrectly).toBe(gameRoom.guessedCorrectly);
  });

  it('should get the drawing player id', () => {
    const player1 = new Player(1, 'hwid1', 'player1');
    const player2 = new Player(2, 'hwid2', 'player2');

    gameRoom.addPlayer(player1);
    gameRoom.addPlayer(player2);

    expect(gameRoom.getDrawingPlayerId()).toBe(player1.id);
  });

  it('should handle the isDrawing boolean', () => {
    const player1 = new Player(1, 'hwid1', 'player1');
    const player2 = new Player(2, 'hwid2', 'player2');

    gameRoom.addPlayer(player1);
    gameRoom.addPlayer(player2);
    gameRoom.handleIsDrawingBoolean();

    expect(player1.getIsDrawing()).toBe(true);
    expect(player2.getIsDrawing()).toBe(false);
  });

  it('should get the game code', () => {
    expect(gameRoom.getGameCode()).toBe(gameRoom.gameCode);
  });

  it('should get the game room id', () => {
    expect(gameRoom.getRoomId()).toBe(gameRoom.id);
  });

  it('should increment the drawing player index', () => {
    const player1 = new Player(1, 'hwid1', 'player1');
    const player2 = new Player(2, 'hwid2', 'player2');
    gameRoom.addPlayer(player1);
    gameRoom.addPlayer(player2);
  
    gameRoom.endDrawingPeriod();
  
    expect(gameRoom.drawingPlayer).toBe(1);
  });
  
  it('should end the game if the drawing player index exceeds the number of players', () => {
    const player1 = new Player(1, 'hwid1', 'player1');
    gameRoom.addPlayer(player1);
  
    gameRoom.endDrawingPeriod();
  
    expect(gameRoom.getGameStatus()).toBe(GameStatus.FINISHED);
  });
  
  it('should set the game status to LEADERBOARD', () => {
    const player1 = new Player(1, 'hwid1', 'player1');
    const player2 = new Player(2, 'hwid2', 'player2');
    gameRoom.addPlayer(player1);
    gameRoom.addPlayer(player2);
  
    gameRoom.endDrawingPeriod();
  
    expect(gameRoom.getGameStatus()).toBe(GameStatus.LEADERBOARD);
  });

  it('should set the isDrawing property of the current drawing player to true if the drawingPlayer is 0', () => {
    const player1 = new Player(1, 'hwid1', 'player1');
    const player2 = new Player(2, 'hwid2', 'player2');
    gameRoom.addPlayer(player1);
    gameRoom.addPlayer(player2);
  
    gameRoom.handleIsDrawingBoolean();
  
    expect(player1.getIsDrawing()).toBe(true);
  });
  
  it('should set the isDrawing property of the previous drawing player to false and the current drawing player to true if the drawingPlayer is not 0', () => {
    const player1 = new Player(1, 'hwid1', 'player1');
    const player2 = new Player(2, 'hwid2', 'player2');
    gameRoom.addPlayer(player1);
    gameRoom.addPlayer(player2);
    gameRoom.drawingPlayer = 1;
  
    gameRoom.handleIsDrawingBoolean();
  
    expect(player1.getIsDrawing()).toBe(false);
    expect(player2.getIsDrawing()).toBe(true);
  });
  
  it('should not change the isDrawing property of any player if the drawingPlayer exceeds the number of players', () => {
    const player1 = new Player(1, 'hwid1', 'player1');
    gameRoom.addPlayer(player1);
    gameRoom.drawingPlayer = 2;
  
    gameRoom.handleIsDrawingBoolean();
  
    expect(player1.getIsDrawing()).toBe(false);
  });
  
});