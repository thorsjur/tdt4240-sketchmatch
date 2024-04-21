package com.groupfive.sketchmatch.navigator.nav_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.groupfive.sketchmatch.navigator.Screen
import com.groupfive.sketchmatch.view.draw.DrawScreenLayout
import com.groupfive.sketchmatch.view.gameroomslist.GamesListScreen
import com.groupfive.sketchmatch.view.leaderboard.Leaderboard
import com.groupfive.sketchmatch.view.mainmenu.HelpScreen
import com.groupfive.sketchmatch.view.mainmenu.MainMenuScreen
import com.groupfive.sketchmatch.view.waitinglobby.WaitingLobby

@Composable
fun SetupNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.MainMenu.route
    ) {

        // Home Screen Destination
        composable(
            route = Screen.MainMenu.route
        ) {
            MainMenuScreen(navController = navController)
        }

        // Help Screen Destination
        composable(
            route = Screen.Help.route
        ) {
            HelpScreen(navController = navController)
        }

        // GameRoomsList Screen Destination
        composable(
            route = Screen.GameRoomsList.route
        ) {
            GamesListScreen(navController = navController)
        }

        // WaitingLobby Screen Destination
        composable(
            route = Screen.WaitingLobby.route
        ) {
            WaitingLobby(navController = navController)
        }

        // Draw Screen Destination
        composable(
            route = Screen.Draw.route
        ) {
            DrawScreenLayout(navController = navController)
        }

        // Leaderboard Destination
        composable(
            route = Screen.Leaderboard.route
        ) {
            Leaderboard(navController = navController)
        }
    }
}