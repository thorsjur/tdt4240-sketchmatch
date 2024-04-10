package com.groupfive.sketchmatch.view.scoreCard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groupfive.sketchmatch.model.Player

@Composable
fun ScoreCard(players: List<Player>) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(4.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        players.sortedByDescending { it.points }.forEachIndexed { index, player ->
            ScoreCardEntry(name = player.name, points = player.points, placement = index + 1)
        }
    }
}

@Composable
fun ScoreCardEntry(name: String, points: Int, placement: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "#$placement",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = name, fontWeight = FontWeight.Bold)
            Text(text = "$points points")
        }
        Icon(imageVector = Icons.Filled.Person, contentDescription = "Avatar")
    }
}