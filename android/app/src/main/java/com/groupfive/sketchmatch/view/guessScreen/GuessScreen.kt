package com.groupfive.sketchmatch.view.guessScreen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.groupfive.sketchmatch.ui.theme.SketchmatchTheme
import com.groupfive.sketchmatch.viewmodel.GuessingViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuessScreen(
    guessingViewModel: GuessingViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            GuessScreenHeader(
                currentRound = guessingViewModel.currentRound,
                numberOfRounds = guessingViewModel.totalNumberOfRounds,
                secondsLeft = guessingViewModel.secondsLeft,
                guessWordLength = guessingViewModel.guessWordLength
            )
        },
        bottomBar = {
            GuessScreenBottomBar(
                guessValue = guessingViewModel.userGuess,
                onGuessValueChange = { guessingViewModel.userGuess = it }
            )
        },
        content = { GuessScreenContent(it) }
    )
    LaunchedEffect(key1 = Unit) {
        guessingViewModel.startCountDown()
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    SketchmatchTheme {
        GuessScreen()
    }
}