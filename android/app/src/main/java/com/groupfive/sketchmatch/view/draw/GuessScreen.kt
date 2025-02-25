package com.groupfive.sketchmatch.view.draw

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.groupfive.sketchmatch.R
import com.groupfive.sketchmatch.models.Event
import com.groupfive.sketchmatch.store.GameData
import com.groupfive.sketchmatch.viewmodels.DrawViewModel
import com.groupfive.sketchmatch.viewmodels.GuessViewModel
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.rememberDrawController
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuessScreen(
    modifier: Modifier = Modifier,
    drawViewModel: DrawViewModel,
    lifecycle: LifecycleOwner = LocalLifecycleOwner.current,
    guessViewModel: GuessViewModel,
) {
    val controller = rememberDrawController()
    var showCorrectnessIcon by rememberSaveable { mutableStateOf(false) }
    val gameRoomId = GameData.currentGameRoom.value?.id ?: 0

    var currentGuess = drawViewModel.currentGuess.value

    val events = guessViewModel.eventsFlow.collectAsState(initial = null)
    val event = events.value // allow Smart cast

    val drawBoxPayLoad by GameData.drawBoxPayLoad.observeAsState()

    // Clear the payload in the controller
    controller.reset()

    // On drawBoxPayLoad change
    if (drawBoxPayLoad != null) {
        controller.importPath(drawBoxPayLoad!!)
    }

    LaunchedEffect(Unit) {
        guessViewModel.handleRender(controller)
    }


    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP, Lifecycle.Event.ON_DESTROY -> {
                    guessViewModel.handleDestroy()
                }

                else -> {}
            }
        }
        lifecycle.lifecycle.addObserver(observer)
        onDispose {
            lifecycle.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp)
                    .border(
                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                        RoundedCornerShape(10.dp)
                    )
            ) {

                Box(modifier = Modifier.fillMaxSize()) {
                    DrawBox(
                        drawController = controller,
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = { }
                                )
                            }
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = { }
                                ) { _, _ -> }
                            },
                        bitmapCallback = { _, _ -> }
                    ) { _, _ -> }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(IntrinsicSize.Max)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(2f),
                    value = currentGuess,
                    onValueChange = { drawViewModel.setCurrentGuess(it) },
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {
                        Text(
                            text = "Enter guess ..."
                        )
                    })
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    enabled = !guessViewModel.isCorrect,
                    onClick = {
                        guessViewModel.checkGuess(currentGuess.lowercase(), gameRoomId)
                    }) {
                    Text(text = "Guess")
                }
            }
        }

        LaunchedEffect(key1 = guessViewModel) {
            guessViewModel.eventsFlow.collect { event ->
                when (event) {
                    is Event.GuessAnswerEvent -> {
                        showCorrectnessIcon = true
                    }
                    null -> {}
                }
            }
        }

        if (showCorrectnessIcon) {
            LaunchedEffect(key1 = Unit) {
                delay(2000)
                showCorrectnessIcon = false
            }

            if (guessViewModel.isCorrect) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(230.dp),
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = stringResource(R.string.correct),
                        tint = Color.Green.copy(alpha = 0.3f)
                    )
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(230.dp),
                        imageVector = Icons.Filled.Cancel,
                        contentDescription = stringResource(R.string.incorrect),
                        tint = Color.Red.copy(alpha = 0.3f),
                    )
                }
            }
        }
    }
}