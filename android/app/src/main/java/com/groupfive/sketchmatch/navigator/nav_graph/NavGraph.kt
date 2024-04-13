package com.groupfive.sketchmatch.navigator.nav_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.groupfive.sketchmatch.GamesListScreen
import com.groupfive.sketchmatch.HelpScreen
import com.groupfive.sketchmatch.MainMenuScreen
import com.groupfive.sketchmatch.navigator.Screen
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition

@Composable
fun SetupNavGraph(navController: NavHostController) {

    /*
    This is a simple NavHost that contains the different screens of the app.
    When we implement more screens, we have to separate the graph into different nested NavHosts.
    We have to combine logically related screens into a single NavHost.
    E.g. We can combine the screens related to a game (draw, score, guess, etc.) into a single NavHost.

    You can follow this tutorial:
    https://www.youtube.com/watch?v=glyqjzkc4fk&list=PLSrm9z4zp4mFYcmFGcJmdsps_lpsaWvKM&index=1

    There is also shown how to implement bottom navigation bar with using the NavHost,
    but I am not sure that we need it. It will create some difficulties with managing the states.
    E.g if we want to go to the main menu from the game screen while the game is in progress,
    we have to do it from a designated button, not from the bottom navigation bar in order to
    properly process the game state (send to the server that the player left the game, etc.)
    And as we do not have so many screens out of the game context, we can use the build in back
    button of the OS to navigate back to the main menu from the help screen for example.
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
    }
}