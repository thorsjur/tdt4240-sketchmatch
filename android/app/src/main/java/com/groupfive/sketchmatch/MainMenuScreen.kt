package com.groupfive.sketchmatch

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.groupfive.sketchmatch.ui.theme.SketchmatchTheme

@Composable
fun MainMenuScreen(modifier: Modifier = Modifier) {
    var shouldShowHelp by rememberSaveable { mutableStateOf(false) }

    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (shouldShowHelp){
                HelpScreen()
            }
            else {
                Text(text = "Sketch Match")
                CreateGameButton(onCreateGameClicked = {})
                JoinGameButton(onJoinGameClicked = {})
                HelpButton(onHelpClicked = { shouldShowHelp = true })
            }
        }
    }
}


@Composable
fun CreateGameButton(onCreateGameClicked: () -> Unit,
               modifier: Modifier = Modifier) {
    Button(
        modifier = Modifier
            .padding(vertical = 3.dp)
            .width(250.dp),
        onClick = onCreateGameClicked
    ) {
        Text(text = "Create game")
    }
}

@Composable
fun JoinGameButton(onJoinGameClicked: () -> Unit,
               modifier: Modifier = Modifier) {
    Button(
        modifier = Modifier
            .padding(vertical = 3.dp)
            .width(250.dp),
        onClick = onJoinGameClicked
    ) {
        Text(text = "Join game")
    }
}

@Composable
fun HelpButton(onHelpClicked: () -> Unit,
               modifier: Modifier = Modifier) {
    Button(
        modifier = Modifier
            .padding(vertical = 3.dp)
            .width(250.dp),
        onClick = onHelpClicked
    ) {
        Text(text = "How to play")
    }
}

@Preview(showBackground = true, widthDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, widthDp = 360)
@Composable
fun MainMenuPreview() {
    SketchmatchTheme {
        MainMenuScreen()
    }
}