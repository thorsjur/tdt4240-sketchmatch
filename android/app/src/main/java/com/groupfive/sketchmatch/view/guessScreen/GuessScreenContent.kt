package com.groupfive.sketchmatch.view.guessScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.groupfive.sketchmatch.model.Player
import com.groupfive.sketchmatch.view.scoreCard.ScoreCard


val mockPlayers = listOf(
    Player("Palina", 234),
    Player("Frida", 157),
    Player("Adele", 324),
    Player("Tsvetelin", 121),
    Player("Sondre", 20),
)

@Composable
fun GuessScreenContent(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier = Modifier
                .weight(4f)
                .fillMaxSize()
                .border(2.dp, MaterialTheme.colorScheme.outlineVariant)
        )
        {
            // Insert the live drawing Composable here.
        }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .fillMaxSize()
                .weight(2f)
                .border(2.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            ScoreCard(players = mockPlayers)
        }
    }
}