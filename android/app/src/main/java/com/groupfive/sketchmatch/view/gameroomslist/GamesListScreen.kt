package com.groupfive.sketchmatch.view.gameroomslist

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.groupfive.sketchmatch.CreateGamePopUp
import com.groupfive.sketchmatch.R
import com.groupfive.sketchmatch.models.GameRoom
import com.groupfive.sketchmatch.navigator.Screen
import com.groupfive.sketchmatch.ui.theme.SketchmatchTheme
import com.groupfive.sketchmatch.viewmodels.GameRoomsViewModel

@Composable
fun GamesListScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: GameRoomsViewModel = viewModel()
    val gameRooms by viewModel.gameRooms.observeAsState()
    var openCreateGamePopup by remember { mutableStateOf(false) }
    var openJoinGameRoomByCodePopup by remember { mutableStateOf(false) }

    val navigateEvent by viewModel.navigateToGameLobby.observeAsState()
    val joinGameByCodeStatus by viewModel.joinGameByCodeStatus.observeAsState(false)
    val joinGameByCodeMessage by viewModel.joinGameByCodeMessage.observeAsState("")

    // Handle back button press
    BackHandler {
        // Remove all callbacks for events from the server as we are leaving the screen and don't need them anymore to update the UI
        viewModel.removeAllCallbacks()
        navController.popBackStack()
    }

    navigateEvent?.getContentIfNotHandled()?.let {
        viewModel.removeAllCallbacks()

        // TODO: Navigate to the game lobby screen in stead of Draw screen
        navController.navigate(Screen.Draw.route)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = modifier
            .fillMaxSize()
            .padding(5.dp, top = 20.dp, bottom = 0.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, bottom = 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Create game button
                Button(
                    onClick = {
                        openCreateGamePopup = true
                            // Handle create game button click here
                        Log.i("GamesListScreen", "Create game button clicked")
                    }
                ) {
                    Text(text = stringResource(id = R.string.create_game_room_button))
                }

                // Spacer
                Spacer(modifier = Modifier.width(15.dp))

                // Join by code button
                Button(
                    onClick = {
                        // Handle join by code button click here
                        Log.i("GamesListScreen", "Join by code button clicked")

                        openJoinGameRoomByCodePopup = true
                    }
                ) {
                    Text(text = stringResource(R.string.join_by_code_button))
                }

                // Spacer
                Spacer(modifier = Modifier.width(15.dp))
            }

            Button(
                modifier = Modifier
                    .padding(16.dp),
                onClick = {
                    // Handle refresh button click here
                    viewModel.refreshGameRooms()
                }
            ) {
                Text(text = stringResource(R.string.refresh))
            }

            CreateGamePopUp(openCreateGamePopup = openCreateGamePopup, onOpenCreateGamePopup = { openCreateGamePopup = it })

            Column(Modifier.height(20.dp)) {
                Text(
                    text = stringResource(R.string.game_list_label),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                )
            }

            LazyColumn(
                modifier = modifier
                    .weight(9f)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                items(items = gameRooms ?: emptyList()) { gameRoom ->
                    GameRoomItem(gameRoom = gameRoom) {
                        // Handle join button click on a game room
                        // Send a request to the server to join the game room
                        viewModel.joinGameByCode(gameRoom.gameCode)
                    }
                }
            }
        }
    }

    if(openJoinGameRoomByCodePopup) {
        JoinGameRoomByCodePopup(
            onSubmit = { gameCode ->
                // Handle join game by code button click here
                Log.i("GamesListScreen", "Joining room with code $gameCode")

                viewModel.joinGameByCode(gameCode)
            },
            onDismiss = { openJoinGameRoomByCodePopup = false },
            onSuccess = {
                Log.i("GamesListScreen", "Joined game room successfully")
                openJoinGameRoomByCodePopup = false
                viewModel.removeAllCallbacks()

                // TODO: Navigate to the game lobby screen
                navController.navigate(Screen.Draw.route)
            }
        )
    }

    // When join button is clicked and server responds with error status
    if (!joinGameByCodeStatus && joinGameByCodeMessage.isNotEmpty()) {
        viewModel.joinGameByCodeMessage.value?.let { GamesListToastMaker(context, it) }
        viewModel.joinGameByCodeStatus.postValue(false)
        viewModel.joinGameByCodeMessage.postValue("")
    }
}

@Composable
fun GameRoomItem(gameRoom: GameRoom, onJoinClicked: (Int) -> Unit) {
    // Display the game room data
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            // Var name get text from var strings and pass the room name as parameter
            Text(text = String.format(stringResource(id = R.string.room_list_row_name),
                gameRoom.gameName))
            Text(text = String.format(stringResource(id = R.string.room_list_row_players),
                gameRoom.players.size,
                gameRoom.gameCapacity))
            Text(text = String.format(stringResource(id = R.string.room_list_row_status),
                gameRoom.gameStatus.name))
        }
        JoinButton(onJoinClicked = { onJoinClicked(gameRoom.id) })
    }

    // Display a divider
    Divider(3)
}

// Join button
@Composable
fun JoinButton(onJoinClicked: () -> Unit) {
    Button(onClick = onJoinClicked) {
        Text(text = stringResource(id = R.string.join_game_room_button))
    }
}

// Divider
@Composable
fun Divider(height: Int = 1) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    ) {}
}

// Toast Maker
@Composable
fun GamesListToastMaker(
    context: Context = LocalContext.current,
    messageId: String
) {
    var message = when(messageId){
        "game_room_already_full" -> stringResource(id = R.string.game_room_already_full)
        "game_room_not_found" -> stringResource(id = R.string.game_room_not_found)
        else -> stringResource(id = R.string.something_went_wrong)
    }

    Toast.makeText(
        context,
        message,
        Toast.LENGTH_SHORT
    ).show()
}

@Preview(showBackground = true, widthDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, widthDp = 360)
@Composable
fun GamesListPreview() {
    SketchmatchTheme {
        GamesListScreen(navController = rememberNavController())
    }
}