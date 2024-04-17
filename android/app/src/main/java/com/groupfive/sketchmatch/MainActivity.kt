package com.groupfive.sketchmatch

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.groupfive.sketchmatch.navigator.nav_graph.SetupNavGraph
import com.groupfive.sketchmatch.ui.theme.SketchmatchTheme
import com.groupfive.sketchmatch.communication.MessageClient

class MainActivity : ComponentActivity() {
    // NavHostController
    private lateinit var navController: NavHostController

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hwid = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)

        // Socket IO Client
        val client = MessageClient.getInstance(hwid)

        // TODO: Check if failed to establish connection and show error message
        client.establishConnection()

        // Get string from resources
        var nicknameSetStatusMessage: String = resources.getString(R.string.set_nickname_success_msg)

        setContent {
            SketchmatchTheme {
                navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }
}