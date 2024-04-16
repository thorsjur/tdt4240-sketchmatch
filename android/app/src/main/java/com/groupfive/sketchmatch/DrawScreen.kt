package com.groupfive.sketchmatch

import DrawViewModel
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.groupfive.sketchmatch.ui.theme.SketchmatchTheme
import com.groupfive.sketchmatch.view.draw.ControlBar
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.rememberDrawController
import io.ak1.rangvikalp.RangVikalp

data class Player(val id: Int, var isComplete: Boolean)

@Composable
fun DrawScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    drawViewModel: DrawViewModel = viewModel()
) {
    val currentWord = drawViewModel.currentWord.value
    val timeCount = drawViewModel.timeCount.value
    val controller = rememberDrawController()

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
                    Spacer(modifier.weight(1f))
                    LeaveGameButton(onLeaveGameClicked = {
                        drawViewModel.goBackToMainMenu(
                            navController
                        )
                    })
                }

                TopWordBar(modifier, currentWord, timeCount, drawViewModel)

                Surface(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(0.dp, 10.dp)
                        .border(
                            BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                            RoundedCornerShape(10.dp)
                        )
                ) {
                    DrawBox(
                        drawController = controller,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f, true),
                        bitmapCallback = { _, _ -> }
                    ) { _, _ ->
                        drawViewModel.hideToolbars()
                    }
                }
                ControlBar(
                    controller = controller,
                    onColorClick = {
                        drawViewModel.toggleColorBarVisibility()
                    },
                    onSizeClick = {
                        drawViewModel.toggleSizePickerVisibility()
                    },
                )
                RangVikalp(isVisible = drawViewModel.isColorBarVisible.value, showShades = true) {
                    drawViewModel.changeColor(it)
                    controller.changeColor(it)
                }
                SizePicker(isVisible = drawViewModel.isSizePickerVisible.value) {
                    controller.changeStrokeWidth(it)
                }

                Row(
                    modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlayersIconsBar(modifier, currentPlayers = drawViewModel.players.value)
                }
            }
        }
    }
}

val sizes = listOf(4f, 8f, 16f, 32f, 48f)

@Composable
fun SizePicker(isVisible: Boolean = false, onSizeSelected: (size: Float) -> Unit) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = {
                it
            },
        ),
        exit = slideOutVertically(
            targetOffsetY = {
                it
            },
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            sizes.forEach {
                SizePickerSizing(size = it, onClick = { onSizeSelected(it) })
            }
        }
    }
}

@Composable
fun SizePickerSizing(size: Float, onClick: () -> Unit) {
    IconButton(onClick = { onClick() }) {
        Box(
            modifier = Modifier
                .requiredSize((size / 2).dp + 6.dp)
                .background(color = Color.Black, shape = CircleShape)
                .border(BorderStroke(Dp.Hairline, MaterialTheme.colorScheme.primary), CircleShape),
        )
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
                WordButton(stringResource(R.string.easy_word), easyWord) { drawViewModel.onWordChosen(easyWord) }
                WordButton(stringResource(R.string.medium_word), mediumWord) { drawViewModel.onWordChosen(mediumWord) }
                WordButton(stringResource(R.string.hard_word), hardWord) { drawViewModel.onWordChosen(hardWord) }
            }
        },
        confirmButton = {
            Button(onClick = { drawViewModel.generateWords() }) {
                stringResource(R.string.new_words)
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
        stringResource(R.string.leave_game)
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
                stringResource(R.string.draw)
            }
            Column(modifier.padding(5.dp, 0.dp)) {
                Text(text = currentWord)
            }
            Spacer(modifier.weight(1f))
            Column(
                modifier.padding(0.dp, 0.dp, 10.dp, 0.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(text = formattedTime)
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
                if (player.isComplete) {
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


@Preview(
    showBackground = true,
    widthDp = 360,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark"
)
@Preview(showBackground = true, widthDp = 360, name = "Light")
@Composable
fun DrawScreenPreview() {
    val viewModel = DrawViewModel()
    SketchmatchTheme {
        DrawScreen(modifier = Modifier, navController = rememberNavController())
    }
}