import express from "express";
import { createServer } from "http";
import { Server } from "socket.io";

// Repositories
import { GameRoomsRepository } from "./Repository/GameRoomsRepository.mjs";
import { PlayersRepository } from "./Repository/PlayersRepository.mjs";

// DTOs
import { CheckGuessRequestDTO } from "./Dto/Request/CheckGuessRequestDTO.mjs";
import { CreateGameRequestDTO } from "./Dto/Request/CreateGameRequestDTO.mjs";
import { JoinGameByCodeRequest } from "./Dto/Request/JoinGameByCodeRequestDTO.mjs";
import { PublishPathRequestDTO } from "./Dto/Request/PublishPathRequestDTO.mjs";
import { RoomEventRequestDTO } from "./Dto/Request/RoomEventRequestDTO.mjs";
import { SetDrawWordRequestDTO } from "./Dto/Request/SetDrawWordRequestDTO.mjs";
import { SetNicknameRequestDTO } from "./Dto/Request/SetNicknameRequestDTO.mjs";
import { AnswerToGuessResponseDTO } from "./Dto/Response/AnswerToGuessResponseDTO.mjs";
import { CreateGameResponseDTO } from "./Dto/Response/CreateGameResponseDTO.mjs";
import { GameRoomUpdateStatusResponseDTO } from "./Dto/Response/GameRoomUpdateStatusResponseDTO.mjs";
import { JoinGameResponseDTO } from "./Dto/Response/JoinGameResponseDTO.mjs";
import { SetDrawWordResponseDTO } from "./Dto/Response/SetDrawWordResponseDTO.mjs";
import { SetNicknameResponseDTO } from "./Dto/Response/SetNicknameResponseDTO.mjs";
import { TimerTickResponseDTO } from "./Dto/Response/TimerTickResponseDTO.mjs";


const app = express();
const port = 40401;

const httpServer = createServer(app);
const io = new Server(httpServer, {
  cors: {
    origin: "*",
    methods: ["*"],
    allowedHeaders: ["*"],
  },
  path: "/socket.io/",
  allowEIO3: true, // This seems to be necessary for the client to connect, i'm assuming the moko socket library is slightly outdated.
});

// Repositories
//
// TODO: Implement the DB in the repos !!!
//
// -----
const playersRepository = new PlayersRepository();
const gameRoomsRepository = new GameRoomsRepository();

io.on("connection", (socket) => {
  var uuid = socket.handshake.query.hwid;
  console.log(`A user connected with UUID: ${uuid}`);

  socket.on("disconnect", () => {
    console.log(`User disconnected with UUID: ${uuid}`);
    
    // Handle player leaving the game room if he is in one
    gameRoomsRepository.handlePlayerLeaving(uuid);

    // Remove player from the repository
    playersRepository.removePlayerByHWID(uuid);
  });

  // On get_rooms event
  socket.on("get_game_rooms", () => {
    // Getting the game rooms from the repository
    var dataToSend = gameRoomsRepository.getGameRooms();

    // Sending the game rooms to the client
    socket.emit("game_room_list", dataToSend);
  });

  // On set_nickname event
  socket.on("set_nickname", (data) => {
    // Convert string json data to DTO object
    let jsonData = JSON.parse(data);

    // Create response object which will be sent to the client
    var response = new SetNicknameResponseDTO();

    try {
      // Create DTO object from the json data sent by the client
      let dto = new SetNicknameRequestDTO();

      // Setting all properties from the json data to the DTO object
      dto.setProperties(jsonData);
      console.log(`Set nickname: ${dto.nickname}`);

      // Add player to the repository
      playersRepository.addPlayer(uuid, dto.nickname);
      var player = playersRepository.getPlayerByHWID(uuid);
      response.player = player;
    } catch (error) {
      console.error(error.message);
      response.status = "error";
      response.message = "set_nickname_error_msg";
    }

    // Emit set_nickname_response only to the sender
    socket.emit("set_nickname_response", response);
  });

  // On create_room event
  socket.on("create_room", (data) => {
    let jsonData = JSON.parse(data);
    var response = new CreateGameResponseDTO();

    try {
      let dto = new CreateGameRequestDTO();
      dto.setProperties(jsonData);

      const player = playersRepository.getPlayerByHWID(uuid);

      const gameRoom = gameRoomsRepository.createGameRoom(
        dto.gameRoomName,
        player,
        dto.roomCapacity
      );

      socket.join(gameRoom.getRoomId());

      response.gameRoom = gameRoom;

      console.log(
        `Creating room: ${gameRoom.gameName} with capacity ${gameRoom.gameCapacity}`
      );
    } catch (error) {
      response.status = "error";
      response.message = "Error creating game room";
    }

    socket.emit("game_room_created_response", response);

    if (response.status == "success") {
      io.emit("game_room_created", response.gameRoom);
    }
  });

  socket.on("publish_path", (data) => {
    const json = JSON.parse(data);
    const dto = new PublishPathRequestDTO();
    dto.setProperties(json);

    io.to(dto.roomId).emit("draw_payload_published", JSON.stringify(dto));
  });

  // TODO: Remove in the cleanup if it is not used
  socket.on("subscribe_to_room", (data) => {
    const json = JSON.parse(data);
    const dto = new RoomEventRequestDTO();
    dto.setProperties(json);

    socket.join(dto.roomId);
  });

  // TODO: Remove in the cleanup if it is not used
  socket.on("unsubscribe_from_room", (data) => {
    const json = JSON.parse(data);
    const dto = new RoomEventRequestDTO();
    dto.setProperties(json);

    //socket.leave(dto.roomId);
  });

  socket.on("leave_room", (data) => {
    console.log(`Leaving room: ${data}`);
    const gameRoom = gameRoomsRepository.getGameRoomByPlayerHwid(uuid);

    if(gameRoom) {
      console.log(`Player ${uuid} has left the game room ${gameRoom.id}`);
      gameRoomsRepository.handlePlayerLeaving(uuid);
      socket.leave(gameRoom.id);
    }
  });

  // On check_guess event
  socket.on("check_guess", (data) => {
      let jsonData = JSON.parse(data);

      try {
          let dto = new CheckGuessRequestDTO();
          dto.setProperties(jsonData);

          const gameRoom = gameRoomsRepository.getGameRoomById(dto.gameRoomId);

          const guessingPlayer = playersRepository.getPlayerByHWID(uuid);
          gameRoomsRepository.handleGuess(gameRoom.id, guessingPlayer.id, dto.inputGuess)
          
      } catch (error) {
          response.status = "error";
          response.message = "Error checking guess";
          console.log(response.message);
      }
  });

  // On set_draw_word event
  socket.on("set_draw_word", (data) => {
      // Convert string json data to DTO object
      let jsonData = JSON.parse(data);

      // Create response object which will be sent to the client
      var response = new SetDrawWordResponseDTO();

      try {
          // Create DTO object from the json data sent by the client
          let dto = new SetDrawWordRequestDTO();

          // Setting all properties from the json data to the DTO object
          dto.setProperties(jsonData);

          const gameRoom = gameRoomsRepository.getGameRoomById(dto.gameRoomId);

          var difficulty = gameRoomsRepository.getDificultyObjByString(dto.difficulty);
        
          // Set draw word in the game room
          gameRoomsRepository.startRound(gameRoom.id, dto.drawWord, difficulty);
          response.gameRoom = gameRoom;

          console.log(
              `Setting draw word: ${dto.drawWord} & Setting difficulty: ${difficulty}`
          );
      } catch (error) {
          response.status = "error";
          response.message = "Error setting draw word";
          console.log(response.message);
      }

      // Emit set_draw_word_response only to the sender
      //socket.emit("set_draw_word_response", response);

      // TODO: Emit only to subscribed players
      //io.emit("set_draw_word_response", response.gameRoom);
  });

  // On join_room_by_code event
  socket.on("join_room_by_code", (data) => {
      let jsonData = JSON.parse(data);

      console.log(`Joining room by code: ${jsonData.gameCode}`);
      var response = new JoinGameResponseDTO();
      try {
          let dto = new JoinGameByCodeRequest();
          dto.setProperties(jsonData);
  
          const player = playersRepository.getPlayerByHWID(uuid);
      
          const gameRoom = gameRoomsRepository.getGameRoomByCode(
              dto.gameCode
          );
      
        if (!gameRoom) {
            response.status = "error";
            response.message = "game_room_not_found";
        } else if (gameRoom.players.length >= gameRoom.gameCapacity) {
            response.status = "error";
            response.message = "game_room_already_full";
        } else {
            gameRoom.addPlayer(player);
      
            socket.join(gameRoom.id);
          
            response.gameRoom = gameRoom.serialize();

            // Emit game_room_updated event to all clients in the room
            io.to(gameRoom.id).emit("player_joined_room", response);
    
            // Emit game_room_updated event to all clients
            io.emit("game_room_updated", gameRoom.serialize());
        }
      } catch (error) {
          response.status = "error";
          response.message = "something_went_wrong";
      }

      // Emit join_room_by_code_response only to the sender
      socket.emit("join_room_response", response);
  });
});

gameRoomsRepository.on('round_has_been_created', gameRoom => {
  console.log(`Round has been created in game room: ${gameRoom.gameName}`);
  let dto = new GameRoomUpdateStatusResponseDTO();
  dto.gameRoom = gameRoom.serialize();

  io.to(gameRoom.id).emit('round_is_created_response', dto);
});

gameRoomsRepository.on('round_has_started', gameRoom => {
  console.log(`Round has started in game room: ${gameRoom.gameName}`);
  let dto = new GameRoomUpdateStatusResponseDTO();
  dto.gameRoom = gameRoom.serialize();

  io.to(gameRoom.id).emit('round_started_response', dto);
});

gameRoomsRepository.on('open_leaderboard', gameRoom => {
  console.log(`Opening leaderboard in game room: ${gameRoom.gameName}`);
  let dto = new GameRoomUpdateStatusResponseDTO();
  dto.gameRoom = gameRoom;

  io.to(gameRoom.id).emit('open_leaderboard_response', dto);  
});

gameRoomsRepository.on('round_timer_tick', (gameRoom) => {
  let dto = new TimerTickResponseDTO();
  dto.timerTick = gameRoom.roundTimestamp;

  io.to(gameRoom.id).emit('round_timer_tick_response', dto);
});

gameRoomsRepository.on("leaderboard_timer_tick", (leaderboardTimer, gameRoom) => {
  console.log(`Leaderboard timer tick in game room: ${gameRoom.gameName}: ${leaderboardTimer}`);
  let dto = new TimerTickResponseDTO();
  dto.timerTick = leaderboardTimer;

  io.to(gameRoom.id).emit('leaderboard_timer_tick_response', dto);
});

gameRoomsRepository.on("round_finished", (gameRoom) => {
  console.log(`Round finished in game room: ${gameRoom.gameName}. Game status: ${gameRoom.gameStatus}`);
  let dto = new GameRoomUpdateStatusResponseDTO();
  dto.gameRoom = gameRoom.serialize();

  io.to(gameRoom.id).emit('round_finished_response', dto); 
});

gameRoomsRepository.on('answer_to_guess', (playerId, isCorrect, gameRoom) => {
  console.log(`Answer to guess in game room: ${gameRoom.gameName}: ${isCorrect}`);
  let dto = new AnswerToGuessResponseDTO();
  dto.isCorrect = isCorrect;
  dto.playerId = playerId;
  dto.gameRoom = gameRoom;

  io.to(gameRoom.id).emit('answer_to_guess_response', dto);
});

gameRoomsRepository.on('player_left_room', gameRoom => {
  console.log(`Player left room: ${gameRoom.gameName}`);
  let dto = new JoinGameResponseDTO();
  dto.gameRoom = gameRoom.serialize();
  io.emit('player_left_room', dto);
  io.emit('game_room_updated', gameRoom.serialize());
});

gameRoomsRepository.on('game_room_destroyed', gameRoom => {
  console.log(`Game room destroyed: ${gameRoom.gameName}`);
  io.emit('game_room_destroyed', gameRoom.serialize());
});

httpServer.listen(port, () => {
    console.log(`listening on *:${port}`);
});