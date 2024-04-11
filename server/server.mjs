import { log } from "console";
import express from "express";
import { createServer } from "http";
import { Server } from "socket.io";

import { Player } from './Models/Player.mjs';
import { GameRoom } from './Models/GameRoom.mjs';
import { GameStatus } from './Models/GameRoom.mjs';

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

// GameRooms list
var gameRooms = [];

io.on("connection", (socket) => {
  console.log("A user connected");

  socket.on("message", (msg) => {
    // Currently just echoing the message back to the clients
    io.emit("message", msg);

    // Log the message
    console.log(msg);
  });

  // On get_rooms event
  socket.on("get_game_rooms", () => {
    socket.emit("game_room_list", gameRooms);
  });

  socket.on("disconnect", () => {
    console.log("A user disconnected");
  });
});

httpServer.listen(port, () => {
  console.log(`listening on *:${port}`);
});

// Populate the gameRooms list with some dummy data
// TODO: Remove this in production!!
function populateGameRooms(numRooms = 3) {
  for (let i = 0; i < numRooms; i++) {
    // Random capacity between 2 and 6
    const gameCapacity = Math.floor(Math.random() * 5) + 2;

    // Random players count between 1 and gameCapacity
    const playersCount = Math.floor(Math.random() * gameCapacity) + 1;

    // Create a new game room
    const gameRoom = new GameRoom(i, `game_${i}`, `Game ${i}`, gameCapacity);
    
    // Add some players to the game room
    for (let j = 0; j < playersCount; j++) {
      const player = new Player(j, `Player ${j}`);
      gameRoom.addPlayer(player);
    }

    // Set the game status based on the number of players
    if (gameCapacity === gameRoom.players.length) {
      gameRoom.changeGameStatus(GameStatus.Playing);
    }

    // Add the game room to the list
    gameRooms.push(gameRoom);
  }
}

populateGameRooms(15);

// Update the game status of the rooms every 5 seconds
// TODO: Remove this in production!!
setInterval(() => {
  gameRooms.forEach((gameRoom) => {
    // Random status
    const status = Math.floor(Math.random() * 3);
    gameRoom.changeGameStatus(Object.values(GameStatus)[status]);

    // Emit the updated game room to all clients
    io.emit("game_room_updated", gameRoom);
  });

  console.log("Game rooms updated");
}, 5000);

// Add one room every 1 seconds
setInterval(() => {
  const gameCapacity = Math.floor(Math.random() * 5) + 2;
  const playersCount = Math.floor(Math.random() * gameCapacity) + 1;

  const gameRoom = new GameRoom(gameRooms.length, `game_${gameRooms.length}`, `Game ${gameRooms.length}`, gameCapacity);

  for (let j = 0; j < playersCount; j++) {
    const player = new Player(j, `Player ${j}`);
    gameRoom.addPlayer(player);
  }

  if (gameCapacity === gameRoom.players.length) {
    gameRoom.changeGameStatus(GameStatus.Playing);
  }

  gameRooms.push(gameRoom);

  io.emit("game_room_created", gameRoom);

  console.log("Game room created");
}, 3000);

// Remove random room every 10 seconds
setInterval(() => {
  const index = Math.floor(Math.random() * gameRooms.length);
  const removedRoom = gameRooms.splice(index, 1)[0];

  io.emit("game_room_destroyed", removedRoom);

  console.log("Game room removed");
}, 3000);