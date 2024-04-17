package com.groupfive.sketchmatch.navigator

sealed class Screen(val route: String) {
    object MainMenu : Screen(route = "main_menu_screen")
    object Help : Screen(route = "help_screen")
    object GameRoomsList : Screen(route = "game_rooms_list_screen")
    object Test: Screen(route = "test_screen")
    object Draw: Screen(route = "draw_screen")
}