package com.groupfive.sketchmatch

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.groupfive.sketchmatch.ui.theme.SketchmatchTheme
import com.groupfive.sketchmatch.view.draw.ControlBar
import com.groupfive.sketchmatch.viewmodels.DrawViewModel
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.rememberDrawController
import io.ak1.rangvikalp.RangVikalp

data class Player(val id: Int, var isComplete: Boolean)

@Composable
fun DrawScreen(
    modifier: Modifier = Modifier,
    drawViewModel: DrawViewModel
) {
    val controller = rememberDrawController()
    Surface(
        modifier = modifier
            .fillMaxWidth()
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
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            controller.insertNewPath(offset)
                        },
                        onDragEnd = { drawViewModel.publishFullDrawBoxPayload(controller) }
                    ) { change, _ ->
                        val newPoint = change.position
                        controller.updateLatestPath(newPoint)
                    }
                },
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
        onUndoClick = { drawViewModel.publishFullDrawBoxPayload(controller) },
        onRedoClick = { drawViewModel.publishFullDrawBoxPayload(controller) },
        onClearClick = { drawViewModel.publishFullDrawBoxPayload(controller) }
    )
    RangVikalp(isVisible = drawViewModel.isColorBarVisible.value, showShades = true) {
        drawViewModel.changeColor(it)
        controller.changeColor(it)
    }
    SizePicker(isVisible = drawViewModel.isSizePickerVisible.value) {
        controller.changeStrokeWidth(it)
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

@Preview(
    showBackground = true,
    widthDp = 360,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark"
)
@Preview(showBackground = true, widthDp = 360, name = "Light")
@Composable
fun DrawScreenPreview(drawViewModel: DrawViewModel = viewModel()) {
    SketchmatchTheme {
        DrawScreen(
            modifier = Modifier,
            drawViewModel = drawViewModel
        )
    }
}