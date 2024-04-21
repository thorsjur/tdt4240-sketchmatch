# SketchMatch Android Application

This is the Android client for the SketchMatch application. It is written in Kotlin and uses the Android SDK and Jetpack Compose for the UI.

## Getting Started

To get started with the client, open the [``android``](/android/) directory in Android Studio.

## Building

The project uses Gradle for building. You can build the apk using the `gradlew` script:

```sh
./gradlew assembleDebug
```

## Running

You can build the APK and immediately install the application on an emulator or a connected device using the `gradlew` script:

```sh
./gradlew installDebug
```

**Note:** If you are used to the Android Studio IDE, you can also run the application by clicking the "Run" button in the IDE with an emulator or a connected device.
You can read more about running the application on the [Android Developer website](https://developer.android.com/studio/run).

## Application Structure

The application is structured into several packages, here are some of the key packages:

1. `com.groupfive.sketchmatch`:
This is the main package of the application. It contains the main activity of the application, which serves as an entrypoint.

2. `com.groupfive.sketchmatch.viewmodels`:
This package contains the ViewModel classes for the application. ViewModels are responsible for preparing and managing the data for an Composeable. For example, [`DrawViewModel`](./app/src/main/java/com/groupfive/sketchmatch/viewmodels/DrawViewModel.kt) is a ViewModel class that manages the game state and logic related to the drawing functionality.

3. `com.groupfive.sketchmatch.models`:
This package contains the data classes for the application. Data classes are used to represent the data used in the application. For example, [`GameRoom`](./app/src/main/java/com/groupfive/sketchmatch/models/GameRoom.kt) is a data class that represents a game room.

4. `com.groupfive.sketchmatch.view`:
This package contains the Composeables for the application. Composeables are the building blocks of the UI in Jetpack Compose. For example, [`DrawScreen`](./app/src/main/java/com/groupfive/sketchmatch/view/draw/DrawScreen.kt) is a Composeable that represents the game screen.

3. `com.groupfive.sketchmatch.communication`:
This package is responsible for all the communication between the client and the server. It defines the different types of socket events and their string representations which clients can send to the server. For example, [`RequestEvent`](./app/src/main/java/com/groupfive/sketchmatch/communication/RequestEvent.kt) is an enum class defining the different types of socket events.

4. `com.groupfive.sketchmatch.navigator`:
This package is responsible for navigation within the application. It defines the different screens and their routes.

5. `com.groupfive.sketchmatch.store`:
This package contains the store class for the application. The store class is responsible for managing the state of the application, and is stored as a static object to serve as a source of global truth, that is, [`GameData`](./app/src/main/java/com/groupfive/sketchmatch/store/GameData.kt) is the store class that manages the state of the game.

### Important Android Libraries

- AndroidX Core KTX: Provides Kotlin-friendly extensions for the Android framework.
- AndroidX Lifecycle: Provides lifecycle-aware components.
- AndroidX Activity Compose: Provides Compose support for activities.
- AndroidX Compose: The new UI toolkit for Android.

### Other Important COTS
1. `io.ak1.drawbox`:
This package contains the `DrawController` class and the `DrawBox` Composeable which is used to control the drawing interface. It provides methods to export and import paths, which are used to draw on the canvas.

2. `dev.icerock.moko:socket-io-android`:
This package contains the Kotlin SocketIo implementation which is used to establish a WebSocket connection with the server. It provides methods to send and receive messages over the WebSocket connection, using the SocketIo protocol.

## Configuration

The application's configuration is located in the [``app/build.gradle.kts``](./app/build.gradle.kts) file. The application connects to the SketchMatch server using a WebSocket connection. The address of the hosted server is specified in the aforementioned file:

```kts
debug {
    buildConfigField("String", "SOCKET_IO_ADDRESS", "\"http://creativecode.tu-varna.bg:40401\"")
}
```

You can change the address to connect from an emulator to a locally hosted server using the following configuration:

```kts
debug {
    buildConfigField("String", "SOCKET_IO_ADDRESS", "\"http://10.0.2.2:40401\"")
}
```

If you want to run the client on a physical device with the local server, you might need to replace the address with the local IP address of the machine hosting the server. To find the local IP address, you can use the following command on MAC OS:

```sh
ipconfig getifaddr en0
```

For Windows or Linux, you can use the following command:

```sh
ipconfig
```
