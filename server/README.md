# Server

## Description

This server is responsible for handling requests and serving data for the SketchMatch application.

## Getting Started

To start the server, follow these steps:

1. Install the required dependencies by running the following command:

```bash
npm install
```

2. Start the server by running the following command:

```bash
node server.mjs
```

## Endpoints

The server is using port 40401, and currently only takes action when receiving specific SocketIO events. The following events are currently supported:

-   `connection`: This event is triggered when a client connects to the server. The server will respond with a `connected` event.
-   `disconnect`: This event is triggered when a client disconnects from the server. The server will respond with a `disconnected` event.
-   `message` (custom event): This event is triggered when a client sends a message to the server. The server will respond by reemitting the message.

Note that these events are only used to demonstrate how the server can handle events. Please feel free to change the handling of these events to fit the requirements of the application, as well as add new events as needed.

Note: Each gameroom has x rounds and each round has x turns (where x is the number of players)
