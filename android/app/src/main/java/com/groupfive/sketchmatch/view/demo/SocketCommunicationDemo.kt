package com.groupfive.sketchmatch.view.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.groupfive.sketchmatch.MESSAGE_EVENT

/**
 * Composable serving as a proof of concept for the use of socket io,
 * additionally can be used for testing purposes.
 *
 * IMPORTANT: This is not to be used in the actual application, and should ONLY
 * be used for verification or testing purposes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocketCommunicationDemo() {
    val viewModel = DemoViewModel()
    var messages = remember { viewModel.messages }
    val client = remember { viewModel.client }

    var text by remember { mutableStateOf("") }

    // Hacky variable for maintaining whether the connect/close button is clicked
    var isConnected by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isConnected) {
                Button(onClick = {
                    client.establishConnection()
                    isConnected = true
                }) {
                    Text(text = "Connect")
                }
            }
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Enter message") })
            Button(onClick = {
                client.sendMessage(text)
            }) {
                Text(text = "send message")
            }
            Button(onClick = {
                client.closeConnection()
                isConnected = false
            }) {
                Text(text = "close")
            }
            messages.forEach {
                Text(text = it)
            }

        }
    }
}

/**
 * ViewModel as part of the socketIO demonstration.
 *
 * Note that this is an application of the MVVM architectural pattern.
 */
class DemoViewModel : ViewModel() {
    var messages = mutableStateListOf<String>()
    val client = MessageClient.getInstance()

    init {
        client.addCallback(MESSAGE_EVENT) { this.pushMessage(it) }
    }

    private fun pushMessage(msg: String) =
        messages.add(msg)
}