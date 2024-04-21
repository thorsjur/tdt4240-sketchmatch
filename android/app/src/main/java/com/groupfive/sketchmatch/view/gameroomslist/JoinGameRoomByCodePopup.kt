package com.groupfive.sketchmatch.view.gameroomslist

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.groupfive.sketchmatch.R
import com.groupfive.sketchmatch.ui.theme.SketchmatchTheme
import com.groupfive.sketchmatch.viewmodels.GameRoomsViewModel

@Composable
fun JoinGameRoomByCodePopup(
    modifier: Modifier = Modifier,
    onSubmit: (String) -> Unit,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val viewModel: GameRoomsViewModel = viewModel()
    val joinGameByCodeStatus by viewModel.joinGameByCodeStatus.observeAsState(false)
    val joinGameByCodeMessage by viewModel.joinGameByCodeMessage.observeAsState("")


    Dialog(onDismissRequest = {
        onDismiss()
    }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            JoinGameRoomByCodePopupContent(
                joinGameByCodeStatus = joinGameByCodeStatus,
                joinGameByCodeMessage = joinGameByCodeMessage,
                onSubmit = { gameCode ->
                    onSubmit(gameCode)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinGameRoomByCodePopupContent(
    joinGameByCodeStatus: Boolean,
    joinGameByCodeMessage: String = "",
    onSubmit: (String) -> Unit
) {
    var gameNameString by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.join_game_by_code),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            modifier = Modifier
                .padding(10.dp),
            textAlign = TextAlign.Center,
        )

        TextField(
            value = gameNameString,
            onValueChange = { gameNameString = it },
            label = { Text(stringResource(id = R.string.game_code)) },
            modifier = Modifier
                .padding(20.dp)
                .width(300.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedIndicatorColor = MaterialTheme.colorScheme.inversePrimary,
                cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                containerColor = MaterialTheme.colorScheme.onPrimary
            ),
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            shape = RoundedCornerShape(8.dp)
        )

        // Error message text
        if (!joinGameByCodeStatus && joinGameByCodeMessage != "") {
            JoinGameRoomByCodeErrorMessage(
                context = LocalContext.current,
                messageId = joinGameByCodeMessage
            )
        }


        Button(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .width(150.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            onClick = {
                onSubmit(gameNameString)
            },
            enabled = gameNameString.isNotBlank(),
        ) {
            Text(stringResource(id = R.string.join_game_room_button))
        }
    }
}

@Composable
fun JoinGameRoomByCodeErrorMessage(
    context: Context = LocalContext.current,
    messageId: String
) {
    val message = when (messageId) {
        "game_room_already_full" -> stringResource(id = R.string.game_room_already_full)
        "game_room_not_found" -> stringResource(id = R.string.game_room_not_found)
        else -> stringResource(id = R.string.something_went_wrong)
    }

    GamesListToastMaker(context = context, messageId = message)
}

@Preview(showBackground = true, widthDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, widthDp = 360)
@Composable
fun JoinGameRoomByCodePopupPreview() {
    SketchmatchTheme {
        JoinGameRoomByCodePopup(
            onSubmit = {},
            onDismiss = {},
            onSuccess = {}
        )
    }
}