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
import { RoundTimerUpdateRequestDTO } from "./Dto/Request/RoundTimerUpdateRequestDTO.mjs";
import { SetDrawWordRequestDTO } from "./Dto/Request/SetDrawWordRequestDTO.mjs";
import { SetNicknameRequestDTO } from "./Dto/Request/SetNicknameRequestDTO.mjs";
import { CheckGuessResponseDTO } from "./Dto/Response/CheckGuessResponseDTO.mjs";
import { CreateGameResponseDTO } from "./Dto/Response/CreateGameResponseDTO.mjs";
import { JoinGameResponseDTO } from "./Dto/Response/JoinGameResponseDTO.mjs";
import { RoundFinishedResponseDTO } from "./Dto/Response/RoundFinishedResponseDTO.mjs";
import { RoundTimerUpdateResponseDTO } from "./Dto/Response/RoundTimerUpdateResponseDTO.mjs";
import { SetDrawWordResponseDTO } from "./Dto/Response/SetDrawWordResponseDTO.mjs";
import { SetNicknameResponseDTO } from "./Dto/Response/SetNicknameResponseDTO.mjs";


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
  var hwid = socket.handshake.query.hwid;
  console.log(`A user connected with HWID: ${hwid}`);

  socket.on("disconnect", () => {
    console.log(`User disconnected with HWID: ${hwid}`);

    // TODO: Remove player from a game room if the game is not started
    // TODO: Remove player from the players repository
    // TODO: Decide what will happend with the game if the game is started
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
      playersRepository.addPlayer(hwid, dto.nickname);
      var player = playersRepository.getPlayerByHWID(hwid);
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

      const player = playersRepository.getPlayerByHWID(hwid);

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

  socket.on("subscribe_to_room", (data) => {
    const json = JSON.parse(data);
    const dto = new RoomEventRequestDTO();
    dto.setProperties(json);

    socket.join(dto.roomId);
  });

  socket.on("unsubscribe_from_room", (data) => {
    const json = JSON.parse(data);
    const dto = new RoomEventRequestDTO();
    dto.setProperties(json);

    socket.leave(dto.roomId);
  });

  socket.on("leave_room", (data) => {
    const json = JSON.parse(data);
    const dto = new RoomEventRequestDTO();
    dto.setProperties(json);

    console.log(`Removing player from game room ${dto.roomId} with hwid ${hwid}`)
    gameRoomsRepository.removePlayerFromGameRoom(hwid, dto.roomId);

    // Emit game_room_updated event to all clients
    console.log(`Emitting game_room_updated event to all clients`);
    const gameRoom = gameRoomsRepository.getGameRoomById(dto.roomId);
    io.emit("game_room_updated", gameRoom);
    socket.leave(dto.roomId);
  });

    // On check_guess event
    socket.on("check_guess", (data) => {
        let jsonData = JSON.parse(data);
        var response = new CheckGuessResponseDTO();

        try {
            let dto = new CheckGuessRequestDTO();
            dto.setProperties(jsonData);

            const gameRoom = gameRoomsRepository.getGameRoomById(
                dto.gameRoomId
            );
            const round = gameRoomsRepository.findCurrentRound(gameRoom);

            const guessingPlayer = playersRepository.getPlayerByHWID(hwid);
            const drawingPlayer = round.getDrawingPlayer();
            const checkedGuess = gameRoomsRepository.checkGuess(
                dto.inputGuess,
                round,
                dto.timestamp,
                guessingPlayer,
                drawingPlayer
            );

            response.guess = checkedGuess;

            console.log(
                `Checking guess: ${checkedGuess.inputGuess} - ${checkedGuess.isCorrect}`
            );

            if (checkedGuess.isCorrect && hwid) {
              console.log("Player guessed correctly, emitting player_guessed_correctly event to room.");
              io.to(dto.gameRoomId).emit("player_guessed_correctly", hwid);
            }
        } catch (error) {
            response.status = "error";
            response.message = "Error checking guess";
            console.log(response.message);
        }

        socket.emit("check_guess_response", response.guess);
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

            const gameRoom = gameRoomsRepository.getGameRoomById(
                dto.gameRoomId
            );
            const round = gameRoomsRepository.findCurrentRound(gameRoom);
         
            // Set draw word in the game room
            gameRoomsRepository.updateDrawWord(dto.drawWord, round);
            gameRoomsRepository.updateDifficulty(dto.difficulty, round);
            response.gameRoom = gameRoom;

            console.log(
                `Setting draw word: ${dto.drawWord} & Setting difficulty: ${dto.difficulty}`
            );
        } catch (error) {
            response.status = "error";
            response.message = "Error setting draw word";
            console.log(response.message);
        }

        // Emit set_draw_word_response only to the sender
        socket.emit("set_draw_word_response", response);

        // TODO: Emit only to subscribed players
        io.emit("set_draw_word_response", response.gameRoom);
    });

    // On Round Timer Update event
    socket.on("round_timer_update", (data) => {
        let jsonData = JSON.parse(data);

        try {

            let dto = new RoundTimerUpdateRequestDTO();
            dto.setProperties(jsonData);

            gameRoomsRepository.startRound(dto.gameRoomId);
            console.log("Round started")

        } catch (error) {
            response.status = "error";
            response.message = "Error updating timer";
            console.log(response.message);
        }
    });
    // On join_room_by_code event
    socket.on("join_room_by_code", (data) => {
        let jsonData = JSON.parse(data);

        console.log(`Joining room by code: ${jsonData.gameCode}`);
        var response = new JoinGameResponseDTO();
        try {
            let dto = new JoinGameByCodeRequest();
            dto.setProperties(jsonData);
    
            const player = playersRepository.getPlayerByHWID(hwid);
       
            const gameRoom = gameRoomsRepository.getGameRoomByCode(
                dto.gameCode
            );
            
            const gameRoomId = gameRoomsRepository.getRoomIdFromGameCode(
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
          
                socket.join(gameRoomId);

                // Try to remove player from the game room on disconnect
                socket.on("disconnect", () => {
                    gameRoomsRepository.removePlayerFromGameRoom(hwid, gameRoomId);
                });
             
                response.gameRoom = gameRoom;
        
                // Emit game_room_updated event to all clients
                console.log(`Emitting game_room_updated event to all clients`);
                io.emit("game_room_updated", gameRoom);
            }
        } catch (error) {
            response.status = "error";
            response.message = "something_went_wrong";
        }

        // Emit join_room_by_code_response only to the sender
        socket.emit("join_room_response", response);
    });
});

gameRoomsRepository.on("timer_tick", (timer, gameRoom) => {
    var responseRoundTimer = new RoundTimerUpdateResponseDTO();
    responseRoundTimer.roundTimerTick = timer;
    // Emit to all clients in the room
    io.to(gameRoom.id).emit("round_timer_update_response", responseRoundTimer);
});

gameRoomsRepository.on("round_finished", (gameRoom) => {
    console.log("Round finished");

    var responseRoundFinished = new RoundFinishedResponseDTO();
    responseRoundFinished.gameRoom = gameRoom;
    // Emit to all clients in the room
    io.to(gameRoom.id).emit("round_finished_response", responseRoundFinished);
});

httpServer.listen(port, () => {
    console.log(`listening on *:${port}`);
});
