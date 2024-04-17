package com.groupfive.sketchmatch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.groupfive.sketchmatch.models.Player

// TODO: Remove the dummy values and standard player list and round number
val p1 = Player("1","hwid1", "Ola", 15)
val p2 = Player("2", "hwid1","Kari", 18)
val p3 = Player("3", "hwid1","Arne", 27)
val p4 = Player("4", "hwid1","Gunn", 24)
val p5 = Player("5", "hwid1","Åse", 19)

@Composable
fun Leaderboard(
        modifier: Modifier = Modifier,
        players: List<Player> = listOf(p1, p2, p3, p4, p5),
        roundNumber: Int = 2
) {
    Surface(modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logog),
                contentDescription = null,
                modifier = Modifier.size(250.dp)
            )
            Text(
                modifier = Modifier.padding(10.dp),
                text = getRoundString(roundNumber, players.size),
                style = MaterialTheme.typography.headlineLarge.copy(
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
        Leaderboard()
    }
}