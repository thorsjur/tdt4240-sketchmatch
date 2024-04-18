package com.groupfive.sketchmatch.navigator.nav_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.groupfive.sketchmatch.DrawScreen
import com.groupfive.sketchmatch.view.gameroomslist.GamesListScreen
import com.groupfive.sketchmatch.view.mainmenu.HelpScreen
import com.groupfive.sketchmatch.Leaderboard
import com.groupfive.sketchmatch.navigator.Screen
import com.groupfive.sketchmatch.view.draw.DrawScreenLayout
import com.groupfive.sketchmatch.view.gameroomslist.GamesListScreen
import com.groupfive.sketchmatch.view.mainmenu.HelpScreen
import com.groupfive.sketchmatch.view.mainmenu.MainMenuScreen
import com.groupfive.sketchmatch.view.waitinglobby.WaitingLobby

@Composable
fun SetupNavGraph(navController: NavHostController) {

    /*
    This is a simple NavHost that contains the different screens of the app.
    When we implement more screens, we have to separate the graph into different nested NavHosts.
    We have to combine logically related screens into a single NavHost.
    E.g. We can combine the screens related to a game (draw, score, guess, etc.) into a single NavHost.

    You can follow this tutorial:
    https://www.youtube.com/watch?v=glyqjzkc4fk&list=PLSrm9z4zp4mFYcmFGcJmdsps_lpsaWvKM&index=1
     */

    NavHost(
        navController = navController,
        startDestination = Screen.MainMenu.route,
        /* Removing transitions animations
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
        */
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
            route = Screen.Draw.route + "/{roomId}"
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