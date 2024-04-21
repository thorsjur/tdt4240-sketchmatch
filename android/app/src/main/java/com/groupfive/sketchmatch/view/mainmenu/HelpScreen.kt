package com.groupfive.sketchmatch.view.mainmenu

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.groupfive.sketchmatch.R
import com.groupfive.sketchmatch.ui.theme.SketchmatchTheme


@Composable
fun HelpScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    BackHandler {
        navController.popBackStack()
    }
    

    val desc1 = arrayOf(stringResource(R.string.desc1_1), stringResource(R.string.desc1_2), stringResource(
        R.string.desc1_3
    ), stringResource(R.string.desc1_4), stringResource(R.string.desc1_5))
    val desc2 = arrayOf(stringResource(R.string.desc2_1), stringResource(R.string.desc2_2), stringResource(
        R.string.desc2_3
    ))
    val desc3 = arrayOf(stringResource(R.string.desc3_1), stringResource(R.string.desc3_2), stringResource(
        R.string.desc3_3
    ), stringResource(R.string.desc3_4), stringResource(R.string.desc3_5), stringResource(R.string.desc3_6), stringResource(
        R.string.desc3_7
    ), stringResource(R.string.desc3_8), stringResource(R.string.desc3_9), stringResource(R.string.desc3_10), stringResource(
        R.string.desc3_11
    ))

    val card1 = arrayOf(stringResource(R.string.creating_a_game_title), desc1)
    val card2 = arrayOf(stringResource(R.string.joining_a_game_title), desc2)
    val card3 = arrayOf(stringResource(R.string.how_to_play_title), desc3)
    val cardList = arrayOf(card1, card2, card3)


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = modifier
            .fillMaxSize()
            .padding(5.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = "How to play",
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }

            LazyColumn(
                modifier = modifier
                    .weight(9f)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                items(items = cardList) { card ->

                    val title = card[0] as String

                    @Suppress("UNCHECKED_CAST")
                    val description = card[1] as Array<String>

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = modifier
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                            .height(IntrinsicSize.Min)
                            .wrapContentHeight()
                    ) {

                        HelpCardContent(title, description)
                    }
                }
            }
        }
    }
}

@Composable
fun HelpCardContent(title: String, description: Array<String>, modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()){
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ), modifier = Modifier.padding(10.dp)
                )

                IconButton(
                    onClick = { expanded = !expanded }) {
                    Icon(
                        modifier = Modifier.size(230.dp),
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (expanded) {
                            stringResource(R.string.show_less)
                        } else {
                            stringResource(R.string.show_more)
                        }
                    )
                }

            }
            if (expanded) {
                NumberedList(description)
            }
        }
    }
}

@Composable
fun NumberedList(textList: Array<String>) {
    val paragraphStyle = ParagraphStyle(textIndent = TextIndent(restLine = 22.sp))

    Text(
        buildAnnotatedString {
            textList.forEach {
                withStyle(style = paragraphStyle) {
                    append("${textList.indexOf(it) + 1}.")
                    append("\t\t")
                    append(it)
                }
            }
        }
    )
}

@Preview(showBackground = true, widthDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, widthDp = 360)
@Composable
fun HelpScreenPreview() {
    SketchmatchTheme {
        HelpScreen(navController = rememberNavController())
    }
}