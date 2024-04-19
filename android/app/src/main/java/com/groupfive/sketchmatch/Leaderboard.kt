package com.groupfive.sketchmatch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.groupfive.sketchmatch.ui.theme.SketchmatchTheme
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.groupfive.sketchmatch.models.Player
import com.groupfive.sketchmatch.viewmodels.LeaderboardViewModel
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.groupfive.sketchmatch.models.GameRoomStatus
import com.groupfive.sketchmatch.models.NavigationEvent
import com.groupfive.sketchmatch.navigator.Screen
import com.groupfive.sketchmatch.store.GameData
import com.groupfive.sketchmatch.utils.getPlayerRankString
import com.groupfive.sketchmatch.utils.getRoundString
import com.groupfive.sketchmatch.viewmodels.RoundTimerUpdateViewModel

@Composable
fun Leaderboard(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: LeaderboardViewModel = viewModel()
) {
    // if drawingplayer.id == currentplayer.id && secondsleft === 0 -> Then navigate to choose word
    val gameRoom by GameData.currentGameRoom.observeAsState()
    val events = viewModel.eventsFlow.collectAsState(initial = null)
    val event = events.value // allow Smart cast

/*
    // the following code block is to navigate only the drawing player to the "choose word view"
//    if (gameRoom?.getCurrentRound()?.drawingPlayer?.id == GameData.currentPlayer.value?.id && leaderboardViewModel.secondsLeft == 0) {
 //       navController.navigate(Screen.Draw.route + "/${gameRoom?.id}")
  //  }
    // Navigate the drawer to the drawing screen
    if (gameRoom?.gameStatus === GameRoomStatus.CHOOSING && gameRoom?.getDrawingPlayerId() === GameData.currentPlayer.value?.id)

    // Navigate everyone to the leaderboard
    if (gameRoom?.gameStatus === GameRoomStatus.LEADERBOARD)
        */

    LaunchedEffect(event) {
        when (event) {
            is NavigationEvent.NavigateDrawerToChoose -> {
                navController.popBackStack()
                navController.navigate(Screen.Draw.route + "/${gameRoom?.id}")
            }
            is NavigationEvent.NavigateToDraw -> {
                navController.popBackStack()
                navController.navigate(Screen.Draw.route + "/${gameRoom?.id}")
            }
            null -> { }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logog),
                contentDescription = null,
                modifier = Modifier.size(250.dp)
            )
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = if (viewModel.secondsLeft != 0) {
                        getRoundString(gameRoom?.getCurrentRoundNumber() ?: 1, gameRoom?.players?.size ?: 1)
                    } else {
                        "${gameRoom?.getDrawingPlayerName()} is choosing a word"
                    },
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                if (viewModel.secondsLeft != 0) {
                    Icon(
                        modifier = Modifier.padding(5.dp),
                        imageVector = Icons.Filled.Alarm,
                        contentDescription = "Alarm Icon"
                    )
                    Text(
                        text = "${viewModel.secondsLeft}",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                LaunchedEffect(key1 = Unit) {
                    viewModel.startCountDown()
                }
            }
            Players(
                modifier = Modifier,
                players = gameRoom?.players ?: emptyList()
            )
        }
    }
}

@Composable
fun Players(
    modifier: Modifier,
    players: List<Player>
) {
    val playersList by rememberSaveable {
        mutableStateOf(players.sortedByDescending { it.score })
    }

    LazyColumn(modifier = modifier) {
        items(items = playersList) { player ->
            val rank = playersList.indexOf(player) + 1
            PlayerCard(modifier, player.nickname, player.score, getPlayerRankString(rank))
        }
    }
}

@Composable
private fun PlayerCard(
    modifier: Modifier,
    name: String,
    score: Int,
    rank: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier,
                    text = rank,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier,
                    text = name,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier,
                    text = score.toString(),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardPreview() {
    SketchmatchTheme {
        Leaderboard(
            navController = rememberNavController(),
            viewModel = LeaderboardViewModel()
        )
    }
}