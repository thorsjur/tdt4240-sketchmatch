package com.groupfive.sketchmatch

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.groupfive.sketchmatch.viewmodels.DrawViewModel
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.rememberDrawController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuessScreen(
    modifier: Modifier = Modifier,
    drawViewModel: DrawViewModel,
    lifecycle: LifecycleOwner = LocalLifecycleOwner.current
) {
    val controller = rememberDrawController()

    LaunchedEffect(Unit) {
        drawViewModel.subscribeToRoom(controller)
    }
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP, Lifecycle.Event.ON_DESTROY -> {
                    drawViewModel.unsubscribeFromRoom()
                }

                else -> {}
            }
        }
        lifecycle.lifecycle.addObserver(observer)
        onDispose {
            lifecycle.lifecycle.removeObserver(observer)
        }
    }

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
            value = drawViewModel.currentGuess.value,
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
            onClick = { drawViewModel.submitGuess() }) {
            Text(text = "Make guess")
        }
    }
}