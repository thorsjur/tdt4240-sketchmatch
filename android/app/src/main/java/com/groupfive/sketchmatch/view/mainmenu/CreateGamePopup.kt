package com.groupfive.sketchmatch.view.mainmenu

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.groupfive.sketchmatch.R
import com.groupfive.sketchmatch.models.Event
import com.groupfive.sketchmatch.navigator.Screen
import com.groupfive.sketchmatch.ui.theme.SketchmatchTheme
import com.groupfive.sketchmatch.view.misc.AlertPopup
import com.groupfive.sketchmatch.viewmodels.CreateGameViewModel

@Composable
fun CreateGamePopUp(
    navController: NavController,
    openCreateGamePopup: Boolean,
    onOpenCreateGamePopup: (Boolean) -> Unit,
    viewModel: CreateGameViewModel = viewModel()
) {
    var numOfPlayers by remember { mutableIntStateOf(2) }
    var gameNameString by remember { mutableStateOf("") }
    val createGameIsError by viewModel.isError.observeAsState(false)

    val events = viewModel.eventsFlow.collectAsState(initial = null)
    val event = events.value // allow Smart cast

    LaunchedEffect(key1 = viewModel) {
        viewModel.eventsFlow.collect { event ->
            when (event) {
                is Event.NavigateToWaitingLobby -> {
                    navController.popBackStack()
                    viewModel.clearAllCallbacks()
                    navController.navigate(Screen.WaitingLobby.route)
                }

                null -> {}
            }
        }
    }

    if (openCreateGamePopup) {
        CreateGameDialog(
            onDismissRequest = { onOpenCreateGamePopup(false) },
            numOfPlayers = numOfPlayers,
            onNumOfPlayersChanged = { newNumOfPlayers -> numOfPlayers = newNumOfPlayers },
            gameNameString = gameNameString,
            onSetGameNameString = { newGameNameString -> gameNameString = newGameNameString },
            onCreateGameRoom = {
                onOpenCreateGamePopup(false)
                viewModel.createGameRoom(gameNameString, numOfPlayers)
            }
        )
    }
    if (createGameIsError) {
        AlertPopup(
            onDismissRequest = { viewModel.isError.postValue(false) },
            onConfirmation = { viewModel.isError.postValue(false) },
            dialogTitle = stringResource(R.string.error),
            dialogText = stringResource(R.string.create_game_error),
            icon = Icons.Filled.ErrorOutline,
            contentDescription = stringResource(R.string.error_outlined)
        )
    }
}

@Composable
fun CreateGameDialog(
    numOfPlayers: Int,
    onNumOfPlayersChanged: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    onCreateGameRoom: () -> Unit,
    gameNameString: String,
    onSetGameNameString: (String) -> Unit,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
        ) {

            CreateGameDialogContent(
                onCreateGameRoom = onCreateGameRoom,
                numOfPlayers = numOfPlayers,
                onNumOfPlayersChanged = onNumOfPlayersChanged,
                gameNameString = gameNameString,
                onSetGameNameString = onSetGameNameString
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGameDialogContent(
    numOfPlayers: Int,
    onNumOfPlayersChanged: (Int) -> Unit,
    onCreateGameRoom: () -> Unit,
    gameNameString: String,
    onSetGameNameString: (String) -> Unit,
) {
    val enableConfirm = gameNameString.isNotBlank() && numOfPlayers in 2..5

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.create_game_room_popup_title),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(10.dp),
            textAlign = TextAlign.Center,
        )
        TextField(
            value = gameNameString,
            onValueChange = { onSetGameNameString(it) },
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.onPrimary
            ),
            placeholder = { Text(stringResource(R.string.create_game_room_popup_example_name)) },
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                .width(300.dp)
        )

        Text(
            text = stringResource(R.string.num_of_players),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(top = 10.dp),
            textAlign = TextAlign.Center,
        )

        Stepper(2, 5, numOfPlayers, onNumOfPlayersChanged)

        Button(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .width(150.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            onClick = onCreateGameRoom,
            enabled = enableConfirm,
        ) {
            Text(text = stringResource(R.string.confirm))
        }
    }
}

@Composable
fun Stepper(
    minValue: Int,
    maxValue: Int,
    chosenValueInt: Int,
    onChosenValueChanged: (Int) -> Unit
) {
    var chosenValueString = chosenValueInt.toString()

    if (chosenValueInt <= minValue) {
        chosenValueString = minValue.toString()
    } else if (chosenValueInt >= maxValue) {
        chosenValueString = maxValue.toString()
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            onClick = { onChosenValueChanged(chosenValueInt - 1) },
            enabled = chosenValueInt > minValue
        ) {
            Icon(
                modifier = Modifier.size(100.dp),
                imageVector = Icons.Filled.ArrowLeft,
                contentDescription = stringResource(R.string.stepper_decrease_value)
            )
        }

        Text(
            text = chosenValueString,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(10.dp),
            textAlign = TextAlign.Center
        )

        IconButton(
            onClick = { onChosenValueChanged(chosenValueInt + 1) },
            enabled = chosenValueInt < maxValue
        ) {
            Icon(
                modifier = Modifier.size(100.dp),
                imageVector = Icons.Filled.ArrowRight,
                contentDescription = stringResource(R.string.stepper_increase_value)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, widthDp = 360)
@Composable
fun PopupPreview() {
    SketchmatchTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            CreateGamePopUp(
                navController = rememberNavController(),
                openCreateGamePopup = false,
                onOpenCreateGamePopup = {}
            )
        }
    }
}