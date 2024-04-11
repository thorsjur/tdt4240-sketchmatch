package com.groupfive.sketchmatch

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import com.groupfive.sketchmatch.models.Player


val p1 = Player("1", "Ola", 10)
val p2 = Player("2", "Kari", 8)
val p3 = Player("3", "Arne", 7)
val p4 = Player("4", "Gunn", 14)

@Composable
fun Scorelist(
        modifier: Modifier = Modifier,
        players: List<Player> = listOf(p1, p2, p3, p4)
    ) {
    Surface(modifier) {
        Column(modifier = modifier.fillMaxSize()) {
            Text(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Scorelist",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            Players(modifier = modifier, players = players)
        }
    }
}

@Composable
fun Players(
    modifier: Modifier,
    players: List<Player>
) {
    var rank = 1
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = players.sortedByDescending { it.score }) { player ->
            PlayerCard(modifier, player.name, player.score, getPlayerRankString(rank))
            rank += 1
        }
    }
}

@Composable
private fun PlayerCard(modifier: Modifier, name: String, score: Int, rank: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .height(IntrinsicSize.Min)
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = rank,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = score.toString(),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScorelistPreview() {
    SketchmatchTheme {
        Scorelist()
    }
}

private fun getPlayerRankString(rank: Int): String {
    when (rank) {
        5 -> return "5th"
        4 -> return "4th"
        3 -> return "3rd"
        2 -> return "2nd"
        1 -> return "1st"
    }
    return "Something wrong has happened"
}