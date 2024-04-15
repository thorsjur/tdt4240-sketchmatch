import { log } from "console";
import express from "express";
import { createServer } from "http";
import { Server } from "socket.io";

// Repositories
import { PlayersRepository } from './Repository/PlayersRepository.mjs';
import { GameRoomsRepository } from './Repository/GameRoomsRepository.mjs';

// DTOs
import { SetNicknameRequestDTO } from './Dto/Request/SetNicknameRequestDTO.mjs';
import { SetNicknameResponsetDTO } from './Dto/Response/SetNicknameResponseDTO.mjs';

import { Player } from "./Models/Player.mjs";
import { GameRoom } from "./Models/GameRoom.mjs";
import { GameStatus } from "./Models/GameRoom.mjs";

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
        var response = new SetNicknameResponsetDTO()
        var hwid = socket.handshake.query.hwid;

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
        const gameRoomName = jsonData.gameRoomName;
        const roomCapacity = jsonData.roomCapacity;

        console.log(
            `Creating room: ${gameRoomName} with capacity ${roomCapacity}`
        );
        const gameRoom = new GameRoom(
            gameRooms.length,
            `game_${gameRooms.length}`,
            gameRoomName,
            roomCapacity
        );

        // TODO: Add room to database
        // TODO: Implement logic for handling status from database

        // Dummy values for now
        const status = "success";
        let message = "";

        if (status === "error") {
            message = "Game room created successfully";
        } else if (status === "success") {
            message = "Error creating game room";
        }

        const response = {
            status: status,
            gameRooms: gameRoom,
            message: message,
        };
        socket.emit("game_room_created_approval", response);

        if (status == "success") {
            io.emit("game_room_created", gameRoom);
        }
    });
});

httpServer.listen(port, () => {
    console.log(`listening on *:${port}`);
});