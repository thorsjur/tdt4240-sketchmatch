# Server

## Description

The server is responsible for handling requests and serving data for the SketchMatch application. It uses Express.js for handling HTTP requests, and Socket.IO for real-time communication between the server and the android clients.

## Getting Started

To start the server, follow these steps:

1. Install the required dependencies by running the following command:

```bash
npm install
```

2. Start the server by running the following command:

```bash
npm start
```
or
```bash
node server.mjs
```

## API

The server communicates with clients using Socket.IO events over the port 40401. The server can emit and listen to various events, including connection, disconnect, get_game_rooms, set_nickname, create_room, publish_path, and others. the complete list of events is as follows:

-   `connection`: This event is triggered when a client connects to the server. The server will respond with a `connected` event.
-   `disconnect`: This event is triggered when a client disconnects from the server. The server will respond with a `disconnected` event.
-   `get_game_rooms`: This event is triggered when a client requests the list of game rooms.
-   `set_nickname`: This event is triggered when a client sets their nickname.
-   `create_room`: This event is triggered when a client requests to create a new game room.
-   `publish_path`: This event is triggered when a client publishes a path.
-   `subscribe_to_room`: This event is triggered when a client subscribes to a room.
-   `unsubscribe_from_room`: This event is triggered when a client unsubscribes from a room.
-   `check_guess`: This event is triggered when a client checks a guess.
-   `set_draw_word`: This event is triggered when a client sets a draw word.
-   `round_timer_update`: This event is triggered when a client updates the round timer.
-   `join_room_by_code`: This event is triggered when a client joins a room by code.


Note that these events are only used to demonstrate how the server can handle events. Please feel free to change the handling of these events to fit the requirements of the application, as well as add new events as needed.

## Architecture
The server is structured into several components:

  - Models: These are the data structures used by the server. They include `GameRoom`, `Player`, `Guess` and `Round`.

  - Repositories: These are the data access layers for the server. They include `GameRoomsRepository` and `PlayersRepository`.

  - DTOs: These are the data transfer objects used for communication between the server and the clients. They include various request and response DTOs.

The server.mjs file is the main entry point of the application. It sets up the HTTP and WebSocket servers, handles client connections, and manages the game logic.

### HTTP Server
The HTTP server is created using Express and the built-in http module. It listens on port 40401.

### WebSocket Server
The WebSocket server is created using Socket.IO. It is configured to allow connections from any origin (*), accept all methods, and allow all headers. It also enables the EIO3 protocol for compatibility with older clients.

## Testing

The server includes a test directory for unit tests. To run the tests, use the following command:
  
  ```bash
  npm test
  ```

To run the tests with coverage, use the following command:

  ```bash
  npm test -- --coverage
  ```

