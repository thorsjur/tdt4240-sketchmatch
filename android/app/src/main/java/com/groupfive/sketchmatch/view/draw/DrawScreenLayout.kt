package com.groupfive.sketchmatch.view.draw

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.groupfive.sketchmatch.DrawScreen
import com.groupfive.sketchmatch.GuessScreen
import com.groupfive.sketchmatch.R
import com.groupfive.sketchmatch.models.Player
import com.groupfive.sketchmatch.viewmodels.DrawViewModel
import com.groupfive.sketchmatch.viewmodels.DrawViewModel.Companion.MAX_ROUNDS


@Composable
fun DrawScreenLayout(
    modifier: Modifier = Modifier,
    navController: NavController,
    drawViewModel: DrawViewModel = viewModel()
) {
    val currentWord = drawViewModel.currentWord.value
    val timeCount = drawViewModel.timeCount.value

    if (drawViewModel.showWordDialog.value) {
        WordChoiceDialog(
            drawViewModel = drawViewModel,
            onDismissRequest = drawViewModel::dismissWordDialog
        )
    } else {
        Surface(modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                modifier.padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                    modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    // The following button is for testing the publishing/subscribing of the drawing.
                    // It can be removed when the proper functionality is implemented.
                    Button(onClick = { drawViewModel.toggleIsDrawing() }) {
                        Text(text = if (drawViewModel.isDrawing.value) "Guess word" else "Draw word")
                    }
                    Spacer(modifier.weight(1f))
                    LeaveGameButton(onLeaveGameClicked = {
                        drawViewModel.goBackToMainMenu(
                            navController
                        )
                    })
                }

                TopWordBar(modifier, currentWord, timeCount, drawViewModel)

                if (drawViewModel.isDrawing.value) {
                    DrawScreen(
                        modifier = Modifier.weight(1f),
                        drawViewModel = drawViewModel
                    )
                } else {
                    GuessScreen(modifier = Modifier.weight(1f), drawViewModel = drawViewModel)
                }

                Row(
                    modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val players = drawViewModel.gameRoom.value?.players
                    if (!players.isNullOrEmpty()) {
                        PlayersIconsBar(
                            modifier,
                            currentPlayers = players
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WordChoiceDialog(
    drawViewModel: DrawViewModel,
    onDismissRequest: () -> Unit
) {
    val easyWord by drawViewModel.easyWord
    val mediumWord by drawViewModel.mediumWord
    val hardWord by drawViewModel.hardWord

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { stringResource(R.string.choose_word) },
        text = {
            Column(verticalArrangement = Arrangement.SpaceAround) {
                WordButton(
                    stringResource(R.string.easy_word),
                    easyWord
                ) { drawViewModel.onWordChosen(easyWord) }
                WordButton(
                    stringResource(R.string.medium_word),
                    mediumWord
                ) { drawViewModel.onWordChosen(mediumWord) }
                WordButton(
                    stringResource(R.string.hard_word),
                    hardWord
                ) { drawViewModel.onWordChosen(hardWord) }
            }
        },
        confirmButton = {
            Button(onClick = { drawViewModel.generateWords() }) {
                Text(text = stringResource(R.string.new_words))
            }
        }
    )
}

@Composable
private fun WordButton(difficulty: String, word: String, onWordChosen: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$difficulty:", modifier = Modifier.weight(1f))
        Button(
            onClick = { onWordChosen(word) },
            modifier = Modifier.weight(2f)
        ) {
            Text(word)
        }
    }
}

@Composable
fun LeaveGameButton(onLeaveGameClicked: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(vertical = 3.dp)
            .width(125.dp),
        onClick = onLeaveGameClicked
    ) {
        Text(text = stringResource(R.string.leave_game))
    }
}


@Composable
fun TopWordBar(
    modifier: Modifier,
    currentWord: String,
    timeCount: Int,
    drawViewModel: DrawViewModel
) {

    var formattedTime = drawViewModel.formatTime(timeCount)

    Surface(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15F),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column(modifier.padding(10.dp, 0.dp, 0.dp, 0.dp)) {
                Text(text = stringResource(if (drawViewModel.isDrawing.value) R.string.draw else R.string.guess) + ":")
            }
            Column(modifier.padding(5.dp, 0.dp)) {
                Text(
                    fontWeight = FontWeight.Bold,
                    text = if (drawViewModel.isDrawing.value) currentWord else "_ ".repeat(
                        currentWord.length
                    )
                )
            }
            Spacer(modifier.weight(1f))
            Row(
                modifier.padding(0.dp, 0.dp, 10.dp, 0.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Alarm, contentDescription = "Alarm icon",
                        tint = MaterialTheme.colorScheme.inverseSurface
                    )
                    Text(text = "$formattedTime")
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Numbers, contentDescription = "Number of rounds",
                        tint = MaterialTheme.colorScheme.inverseSurface
                    )
                    Text(text = "${drawViewModel.currentRound.value}/${MAX_ROUNDS}")
                }
            }
        }
    }
}


@Composable
fun PlayersIconsBar(modifier: Modifier, currentPlayers: List<Player>) {
    Row(modifier.padding(8.dp)) {
        currentPlayers.forEach { player ->
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.player_icon),
                    contentDescription = "Player ${player.id}",
                    modifier = Modifier.size(50.dp)
                )

                // If the player's action is complete, overlay a green-tinted checkmark
                if (player.hasGuessedCorrectly) {
                    Box(
                        modifier = Modifier.size(45.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(shape = MaterialTheme.shapes.extraLarge) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Background",
                                tint = Color.Green.copy(alpha = 0.3f),
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(Color.Green.copy(alpha = 0.4f))
                            )
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Completed",
                                tint = Color.White, // White checkmark
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}