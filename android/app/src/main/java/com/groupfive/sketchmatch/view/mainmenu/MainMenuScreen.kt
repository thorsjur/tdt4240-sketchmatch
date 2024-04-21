package com.groupfive.sketchmatch.view.mainmenu

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.groupfive.sketchmatch.R
import com.groupfive.sketchmatch.navigator.Screen
import com.groupfive.sketchmatch.store.GameData
import com.groupfive.sketchmatch.ui.theme.SketchmatchTheme

@Composable
fun MainMenuScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val context = LocalContext.current

    var openCreateGamePopup by remember { mutableStateOf(false) }
    val player by GameData.currentPlayer.observeAsState()

    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = modifier
                .padding(vertical = 80.dp, horizontal = 25.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                painter = painterResource(id = R.drawable.logog),
                contentDescription = null,
                modifier = Modifier.size(250.dp)
            )
            if (player?.nickname != null && player?.nickname!!.isNotEmpty()) {
                CreateGreetingsText(username = player?.nickname!!)
            } else {
                Spacer(modifier = Modifier.padding(bottom = 50.dp))
            }
            CreateGameButton(onCreateGameClicked = { openCreateGamePopup = true })
            JoinGameButton(onJoinGameClicked = { navController.navigate(Screen.GameRoomsList.route) })
            HelpButton(onHelpClicked = { navController.navigate(Screen.Help.route) })

            CreateGamePopUp(
                navController = navController,
                openCreateGamePopup = openCreateGamePopup,
                onOpenCreateGamePopup = { openCreateGamePopup = it }
            )
        }
    }

    if (player?.nickname.isNullOrEmpty()) {
        UsernameEnterDialog(
            modifier = Modifier,
            onSuccess = { _ ->
                // Show a toast message to the user
                Toast.makeText(
                    context,
                    context.getString(R.string.set_nickname_success_msg),
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}


@Composable
fun CreateGameButton(onCreateGameClicked: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(vertical = 3.dp)
            .width(250.dp),
        onClick = onCreateGameClicked
    ) {
        Text(stringResource(R.string.main_menu_create_game_button))
    }
}

@Composable
fun JoinGameButton(onJoinGameClicked: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(vertical = 3.dp)
            .width(250.dp),
        onClick = onJoinGameClicked
    ) {
        Text(stringResource(R.string.main_menu_join_game_button))
    }
}

@Composable
fun HelpButton(onHelpClicked: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(vertical = 3.dp)
            .width(250.dp),
        onClick = onHelpClicked
    ) {
        Text(stringResource(R.string.main_menu_how_to_play_button))
    }
}

@Composable
fun CreateGreetingsText(
    username: String
) {
    Text(
        text = String.format(stringResource(id = R.string.greetings_message), username),
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(bottom = 20.dp)
    )
}

@Preview(showBackground = true, widthDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, widthDp = 360)
@Composable
fun MainMenuPreview() {
    SketchmatchTheme {
        MainMenuScreen(navController = rememberNavController())
    }
}