package com.groupfive.sketchmatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.navigator.nav_graph.SetupNavGraph
import com.groupfive.sketchmatch.ui.theme.SketchmatchTheme
import java.util.UUID

class MainActivity : ComponentActivity() {
    // NavHostController
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uuid = UUID.randomUUID().toString()

        // Socket IO Client
        val client = MessageClient.getInstance(uuid)

        // TODO: Check if failed to establish connection and show error message
        client.establishConnection()

        // Get string from resources
        var nicknameSetStatusMessage: String =
            resources.getString(R.string.set_nickname_success_msg)

        setContent {
            SketchmatchTheme {
                navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }
}