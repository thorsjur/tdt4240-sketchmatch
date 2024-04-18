package com.groupfive.sketchmatch.view.misc

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.groupfive.sketchmatch.R

@Composable
fun AlertPopup(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    contentDescription: String,
) {
    AlertDialog(
        icon = {
            Icon(icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onPrimaryContainer)
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(stringResource(R.string.ok))
            }
        },
    )
}

//@Composable
//fun DialogExamples() {
//    var openAlertDialog by remember { mutableStateOf(false) }
//
//    openAlertDialog = true
//    AlertDialogExample(
//        onDismissRequest = { openAlertDialog = false },
//        onConfirmation = { openAlertDialog = false},
//        dialogTitle = stringResource(R.string.error),
//        dialogText = stringResource(R.string.create_game_error),
//        icon = Icons.Filled.ErrorOutline,
//        contentDescription = stringResource(R.string.error_outlined)
//    )
//}
//
//@Preview(showBackground = true, widthDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Preview(showBackground = true, widthDp = 360)
//@Composable
//fun AlertPopupPreview() {
//    SketchmatchTheme {
//        Surface (modifier = Modifier.fillMaxSize()){
//            DialogExamples()
//        }
//    }
//}
