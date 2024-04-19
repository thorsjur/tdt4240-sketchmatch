# SketchMatch

SketchMatch is a multiplayer online game where players compete by drawing and guessing sketches based on given words. This repository contains both the Android client and the server for the SketchMatch application.

## Table of Contents

1. [Getting Started](#getting-started)
2. [Building](#building)
3. [Running](#running)
4. [Project Structure](#project-structure)

## Getting Started

To get started with the client, open the [`android`](/android/) directory in Android Studio. For the server, navigate to the [`server`](/server/) directory.

### Prerequisites

- Android Studio
- Node.js for the server
- An Android device or emulator for testing the client

## Building

### Android Client

The Android client uses Gradle for building. You can build the project using the `gradlew` script:

```sh
./gradlew assembleDebug
```

### Server

The server uses npm for building. Navigate to the [``server``](./server/) directory and install the dependencies:

```sh
npm install
```

## Running

### Android Client

You can run the application on an emulator or a connected device using the `gradlew` script:

```sh
./gradlew installDebug
```

#### Connecting to local server
For instructions on how to connect the Android client to the local server, refer to the [Android README Configuration](./android/README.md#configuration) section.

### Server

You can start the server by running the following command in the [``server``](./server/) directory:

```sh
npm start
```

## Project Structure

The project is structured into several packages and directories:

### Android Client

Some of the key packages in the Android client are `com.groupfive.sketchmatch`, `com.groupfive.sketchmatch.viewmodels`, `com.groupfive.sketchmatch.models` and `com.groupfive.sketchmatch.view`. For a more detailed description of the structure, refer to the [Android README](./android/README.md).

### Server

The server is responsible for handling requests and serving data for the SketchMatch application. It uses Express.js for handling HTTP requests, and Socket.IO for real-time communication between the server and the android clients. Some of the key components in the server are `models`, `repositories`, and `dtos`. For more information, refer to the [Server README](./server/README.md).
