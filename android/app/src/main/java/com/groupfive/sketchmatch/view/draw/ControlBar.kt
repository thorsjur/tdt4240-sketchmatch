package com.groupfive.sketchmatch.view.draw

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.ak1.drawbox.DrawController

@Composable
fun ControlBar(
    controller: DrawController,
    onColorClick: () -> Unit,
    onSizeClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        MenuItem(
            Icons.Filled.Undo,
            "undo",
        ) {
            controller.unDo()
        }
        MenuItem(
            Icons.Filled.Redo,
            "redo",
        ) {
            controller.reDo()
        }
        MenuItem(
            Icons.Filled.Clear,
            "reset",
        ) {
            controller.reset()
        }
        MenuItem(
            Icons.Filled.ColorLens,
            "color",
        ) {
            onColorClick()
        }
        MenuItem(Icons.Filled.Brush, "brush size") {
            onSizeClick()
        }
    }
}

@Composable
fun RowScope.MenuItem(
    icon: ImageVector,
    description: String,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick, modifier = Modifier.weight(1f, true)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            modifier = Modifier.size(24.dp)
        )
    }
}