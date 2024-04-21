package com.groupfive.sketchmatch.view.leaderboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.groupfive.sketchmatch.R
import com.groupfive.sketchmatch.models.Event
import com.groupfive.sketchmatch.models.GameRoomStatus
import com.groupfive.sketchmatch.models.Player
import com.groupfive.sketchmatch.navigator.Screen
import com.groupfive.sketchmatch.store.GameData
import com.groupfive.sketchmatch.ui.theme.SketchmatchTheme
import com.groupfive.sketchmatch.utils.getPlayerRankString
import com.groupfive.sketchmatch.utils.getRoundString
import com.groupfive.sketchmatch.view.draw.LeaveGameButton
import com.groupfive.sketchmatch.viewmodels.LeaderboardViewModel

@Composable
fun Leaderboard(
    navController: NavController,
    viewModel: LeaderboardViewModel = viewModel()
) {
    val gameRoom by GameData.currentGameRoom.observeAsState()
    val events = viewModel.eventsFlow.collectAsState(initial = null)
    val event = events.value // allow Smart cast
    val roundNumber = gameRoom?.getCurrentRoundNumber() ?: 1
    val totalNumberOfRounds = gameRoom?.getTotalNumberOfRounds() ?: 2



    LaunchedEffect(event) {
        when (event) {
            is Event.NavigateDrawerToChoose -> {
                viewModel.clearAllCallbacks()
                navController.popBackStack()
                navController.navigate(Screen.Draw.route)
            }

            is Event.NavigateToDraw -> {
                viewModel.clearAllCallbacks()
                navController.popBackStack()
                navController.navigate(Screen.Draw.route)
            }

            null -> {}
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val gameStatus = gameRoom?.gameStatus
                val title = if (gameStatus == GameRoomStatus.FINISHED) {
                    "Game over!"
                } else if (viewModel.secondsLeft != 0) {
                    //"Round ${gameRoom?.getCurrentRoundNumber()}"
                    getRoundString(roundNumber, totalNumberOfRounds)
                } else {
                    "${gameRoom?.getDrawingPlayerName()} is choosing a word"
                }

                Text(

                    modifier = Modifier.padding(10.dp),
                    text = title,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )

                if (gameStatus != GameRoomStatus.FINISHED &&
                    viewModel.secondsLeft != 0
                ) {
                    Icon(
                        modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp),
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

                if (gameStatus == GameRoomStatus.FINISHED) {
                    LeaveGameButton(onLeaveGameClicked = {
                        viewModel.goBackToMainMenu(
                            navController
                        )
                    })
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
            modifier = Modifier.padding(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp), //.weight(1f),
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
                modifier = Modifier.padding(horizontal = 8.dp), //.weight(1f),
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