package edu.agh.susgame.front.ui.component.menu.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class MenuRoute(val route: String) {
    data object MainMenu : MenuRoute("main-menu")

    data object SearchGame : MenuRoute("search-game")

    data object CreateGame : MenuRoute("create-game")

    data object AwaitingGame : MenuRoute("awaiting-game") {
        val gameIdArgument = navArgument("game_id") {
            type = NavType.IntType
            nullable = false
        }
    }

    data object Game : MenuRoute("game") {
        val gameIdArgument = navArgument("game_id") {
            type = NavType.IntType
            nullable = false
        }
    }
}