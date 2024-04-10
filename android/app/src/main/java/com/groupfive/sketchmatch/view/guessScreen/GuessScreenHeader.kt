package com.groupfive.sketchmatch.view.guessScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GuessScreenHeader(
    currentRound: Int,
    numberOfRounds: Int,
    secondsLeft: Int,
    guessWordLength: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .height(64.dp)
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1F),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Filled.Alarm, contentDescription = "Alarm icon",
                    tint = MaterialTheme.colorScheme.inverseSurface
                )
                Text(text = "$secondsLeft s")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Filled.Numbers, contentDescription = "Number of rounds",
                    tint = MaterialTheme.colorScheme.inverseSurface
                )
                Text(text = "$currentRound/$numberOfRounds")
            }
        }
        Column(
            modifier = Modifier
                .weight(1F),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Guessword",
            )
            Text(
                text = "_ ".repeat(guessWordLength),
            )
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.weight(1F),
        ) {
            // This can be removed if we're not using it.
            Icon(
                imageVector = Icons.Filled.Settings, contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.inverseSurface
            )
        }
    }
}