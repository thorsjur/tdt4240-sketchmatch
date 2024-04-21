package com.groupfive.sketchmatch.view.mainmenu

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.groupfive.sketchmatch.R
import com.groupfive.sketchmatch.ui.theme.SketchmatchTheme
import com.groupfive.sketchmatch.viewmodels.SetNicknameViewModel

@SuppressLint("HardwareIds")
@Composable
fun UsernameEnterDialog(
    modifier: Modifier = Modifier,
    onSuccess: (String) -> Unit,
    viewModel: SetNicknameViewModel = viewModel()
){
    val setNicknameIsSuccess by viewModel.nicknameSetStatus.observeAsState(false)
    val nickname by viewModel.nickname.observeAsState("")

    if (!setNicknameIsSuccess && nickname.isEmpty()) {
        Dialog(onDismissRequest = { }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .wrapContentHeight()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
            ) {
                UsernameEnterDialogContent(
                    onSubmit = { username ->
                        viewModel.setNickname(username)
                    }
                )
            }
        }
    }

    if (setNicknameIsSuccess) {
        onSuccess(nickname)
        viewModel.nicknameSetStatus.postValue(false)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameEnterDialogContent(
    onSubmit: (String) -> Unit
) {
    var gameNameString by remember { mutableStateOf("") }

    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Text(
            text = stringResource(id = R.string.set_nickname_modal_title),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            modifier = Modifier
                .padding(10.dp),
            textAlign = TextAlign.Center,
        )

        TextField(
            value = gameNameString,
            onValueChange = { gameNameString = it },
            label = { Text(stringResource(id = R.string.nickname)) },
            modifier = Modifier
                .padding(20.dp)
                .width(300.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedIndicatorColor = MaterialTheme.colorScheme.inversePrimary,
                cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                containerColor = MaterialTheme.colorScheme.onPrimary
            ),
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Button(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .width(150.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer),
            onClick = {
                onSubmit(gameNameString)
            },
            enabled = gameNameString.isNotBlank(),
        ) {
            Text(stringResource(id = R.string.submit))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, widthDp = 360)
@Composable
fun UsernameEnterDialogPreview() {
    SketchmatchTheme {
        UsernameEnterDialog(
            onSuccess = { _ ->
            }
        )
    }
}