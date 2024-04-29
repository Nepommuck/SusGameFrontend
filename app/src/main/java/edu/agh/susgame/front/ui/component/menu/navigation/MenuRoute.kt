package edu.agh.susgame.front.ui.component.menu.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class MenuRoute(val route: String) {
    data object FindGame : MenuRoute("find-game-route")

    data object Profile : MenuRoute("profile-route")

    data object Settings : MenuRoute("settings-route")

    data object AwaitingGame : MenuRoute("awaiting-game-route") {
        val gameIdArgument = navArgument("game_id") {
            type = NavType.IntType
            nullable = false
        }
    }

    data object GameView : MenuRoute("ongoing-game-route") {
        val gameIdArgument = navArgument("game_id") {
            type = NavType.IntType
            nullable = false
        }
    }
}