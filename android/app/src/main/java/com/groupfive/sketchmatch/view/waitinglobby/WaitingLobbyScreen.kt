package com.groupfive.sketchmatch.view.waitinglobby

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.groupfive.sketchmatch.R
import com.groupfive.sketchmatch.navigator.Screen
import com.groupfive.sketchmatch.store.GameData
import com.groupfive.sketchmatch.ui.theme.SketchmatchTheme
import com.groupfive.sketchmatch.viewmodels.WaitingLobbyViewModel
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.groupfive.sketchmatch.models.NavigationEvent
import com.groupfive.sketchmatch.viewmodels.CreateGameViewModel

@Composable
fun WaitingLobby(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: WaitingLobbyViewModel = viewModel()
){
    val player by GameData.currentPlayer.observeAsState()
    val gameRoom by GameData.currentGameRoom.observeAsState()
    val gameRoomCode = gameRoom?.gameCode ?: "Unknown Code"
    val playersWaiting = "${gameRoom?.players?.size ?: 0} / ${gameRoom?.gameCapacity ?: 0}"

    val events = viewModel.eventsFlow.collectAsState(initial = null)
    val event = events.value // allow Smart cast

    LaunchedEffect(event) {
        when (event) {
            is NavigationEvent.NavigateToDraw -> {
                viewModel.clearCallbacks()
                // Remove the current screen from the back stack
                navController.popBackStack()
                // Navigate to the draw screen
                navController.navigate(Screen.Draw.route + "/${gameRoom?.id}")
            }
            null -> { }
        }
    }

    Surface (modifier.fillMaxSize()){
        var title = stringResource(R.string.waiting_lobby_title)
        if((gameRoom?.players?.size ?: 0) == (gameRoom?.gameCapacity ?: 2)){
            title = stringResource(R.string.starting_the_game)
        }
        AlertDialog(
            onDismissRequest = {},
            title = { Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 19.sp)
            ) },
            text = {
                Column(verticalArrangement = Arrangement.SpaceAround) {
                    Row (
                        modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = String.format(stringResource(R.string.players_waiting),playersWaiting),
                            textAlign = TextAlign.Center
                        )
                    }
                    Text(
                        text = stringResource(R.string.game_room_code),
                        modifier = Modifier.fillMaxWidth().padding(0.dp,10.dp,0.dp,0.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = gameRoomCode,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontSize = 30.sp)
                    )
                }
            },
            confirmButton = {
                Button(onClick = { navController.navigate(Screen.MainMenu.route) },
                    modifier
                        .fillMaxWidth()
                        .wrapContentWidth()) {
                    Text(text = stringResource(R.string.back_to_main_menu))
                }
            }
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark"
)
@Preview(showBackground = true, widthDp = 360, name = "Light")
@Composable
fun WaitingLobbyPreview() {
    SketchmatchTheme {
        WaitingLobby(
            modifier = Modifier,
            navController = rememberNavController()
        )
    }
}
