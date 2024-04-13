package com.groupfive.sketchmatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.groupfive.sketchmatch.navigator.nav_graph.SetupNavGraph
import com.groupfive.sketchmatch.ui.theme.SketchmatchTheme
import com.groupfive.sketchmatch.view.demo.MessageClient

class MainActivity : ComponentActivity() {
    // NavHostController
    lateinit var navController: NavHostController

    // Socket IO Client
    private val client = MessageClient.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Check if failed to establish connection and show error message
        client.establishConnection()

        setContent {
            SketchmatchTheme {
                navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }
}