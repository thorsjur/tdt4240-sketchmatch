import express from "express";
import { createServer } from "http";
import { Server } from "socket.io";

// Repositories
import { GameRoomsRepository } from "./Repository/GameRoomsRepository.mjs";
import { PlayersRepository } from "./Repository/PlayersRepository.mjs";

// DTOs
import { CreateGameRequestDTO } from "./Dto/Request/CreateGameRequestDTO.mjs";
import { JoinGameByCodeRequest } from './Dto/Request/JoinGameByCodeRequestDTO.mjs';
import { PublishPathRequestDTO } from "./Dto/Request/PublishPathRequestDTO.mjs";
import { RoomEventRequestDTO } from "./Dto/Request/RoomEventRequestDTO.mjs";
import { SetNicknameRequestDTO } from "./Dto/Request/SetNicknameRequestDTO.mjs";
import { CreateGameResponseDTO } from "./Dto/Response/CreateGameResponseDTO.mjs";
import { JoinGameResponseDTO } from './Dto/Response/JoinGameResponseDTO.mjs';
import { SetNicknameResponsetDTO } from "./Dto/Response/SetNicknameResponseDTO.mjs";


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
    var hwid = socket.handshake.query.hwid;
    console.log(`User disconnected with HWID: ${hwid}`);

    // TODO: Remove player from a game room if the game is not started
    // TODO: Remove player from the players repository
    // TODO: Decide what will happend with the game if the game is started
  });

    // On get_rooms event
    socket.on("get_game_rooms", async () => {
        // Getting the game rooms from the repository
        var dataToSend = await gameRoomsRepository.getGameRooms();

    // Sending the game rooms to the client
    socket.emit("game_room_list", dataToSend);
  });

    // On set_nickname event
    socket.on("set_nickname", async (data) => {
        // Convert string json data to DTO object
        let jsonData = JSON.parse(data);

    // Create response object which will be sent to the client
    var response = new SetNicknameResponsetDTO();
    var hwid = socket.handshake.query.hwid;

    try {
      // Create DTO object from the json data sent by the client
      let dto = new SetNicknameRequestDTO();

      // Setting all properties from the json data to the DTO object
      dto.setProperties(jsonData);
      console.log(`Set nickname: ${dto.nickname}`);

            // Add player to the repository
            playersRepository.addPlayer(hwid, dto.nickname);
            var player = await playersRepository.getPlayerByHWID(hwid);
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
    socket.on("create_room", async (data) => {
        let jsonData = JSON.parse(data);
        var response = new CreateGameResponseDTO();

    try {
      let dto = new CreateGameRequestDTO();
      dto.setProperties(jsonData);

            const player = await playersRepository.getPlayerByHWID(hwid);

      const gameRoom = gameRoomsRepository.createGameRoom(
        dto.gameRoomName,
        player,
        dto.roomCapacity
      );

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

    // On join_room_by_code event
    socket.on("join_room_by_code", async (data) => {
        let jsonData = JSON.parse(data);

        console.log(`Joining room by code: ${jsonData.gameCode}`);
        
        var response = new JoinGameResponseDTO();

        try {
            let dto = new JoinGameByCodeRequest();
            dto.setProperties(jsonData);

            const player = await playersRepository.getPlayerByHWID(hwid);
            const gameRoom = await gameRoomsRepository.getGameRoomByCode(dto.gameCode);

            if (!gameRoom) {
                response.status = "error";
                response.message = "game_room_not_found";
            } else if (gameRoom.players.length >= gameRoom.gameCapacity) {
                response.status = "error";
                response.message = "game_room_already_full";
            } else {
                gameRoomsRepository.joinGameRoom(dto.gameCode, player);
                const updatedGameRoom = await gameRoomsRepository.getGameRoomByCode(dto.gameCode);
                response.gameRoom = updatedGameRoom;

                // Emit game_room_updated event to all clients
                console.log(`Emitting game_room_updated event to all clients`);
                io.emit("game_room_updated", updatedGameRoom);
            }
        } catch (error) {
            response.status = "error";
            response.message = "something_went_wrong";
        }

        // Emit join_room_by_code_response only to the sender
        socket.emit("join_room_response", response);
    });
});

httpServer.listen(port, () => {
  console.log(`listening on *:${port}`);
});
