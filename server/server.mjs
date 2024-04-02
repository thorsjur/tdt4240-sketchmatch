import express from "express";
import { createServer } from "http";
import { Server } from "socket.io";

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

io.on("connection", (socket) => {
  console.log("A user connected");

  socket.on("message", (msg) => {
    // Currently just echoing the message back to the clients
    io.emit("message", msg);
  });

  socket.on("disconnect", () => {
    console.log("A user disconnected");
  });
});

httpServer.listen(port, () => {
  console.log(`listening on *:${port}`);
});
