package edu.agh.susgame.front.ui.component.menu.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.agh.susgame.front.model.AwaitingGame
import edu.agh.susgame.front.providers.interfaces.AppSettingsProvider
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider
import edu.agh.susgame.front.providers.interfaces.ServerMapProvider
import edu.agh.susgame.front.ui.component.game.map.MapViewComponent
import edu.agh.susgame.front.ui.component.menu.AwaitingGameComponent
import edu.agh.susgame.front.ui.component.menu.ProfileComponent
import edu.agh.susgame.front.ui.component.menu.SearchGamesComponent
import edu.agh.susgame.front.ui.component.menu.SettingsComponent

@Composable
fun MenuNavigationHost(
    padding: PaddingValues,
    navController: NavHostController,
    serverMapProvider: ServerMapProvider,
    appSettingsProvider: AppSettingsProvider,
    awaitingGamesProvider: AwaitingGamesProvider,
) {
    NavHost(
        navController = navController,
        startDestination = MenuRoute.FindGame.route,
        modifier = Modifier.padding(padding)
    ) {
        composable(MenuRoute.FindGame.route) {
            SearchGamesComponent(awaitingGamesProvider, navController)
        }
        composable(MenuRoute.Profile.route) {
            ProfileComponent()
        }
        composable(MenuRoute.Settings.route) {
            SettingsComponent(appSettingsProvider)
        }
        composable(
            "${MenuRoute.AwaitingGame.route}/{${MenuRoute.AwaitingGame.gameIdArgument.name}}",
            arguments = listOf(MenuRoute.AwaitingGame.gameIdArgument),
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments
                ?.getInt(MenuRoute.AwaitingGame.gameIdArgument.name)
                ?.run {
                    AwaitingGame.AwaitingGameId(this)
                }
            AwaitingGameComponent(gameId, awaitingGamesProvider, navController)
        }
        composable(
            "${MenuRoute.GameView.route}/{${MenuRoute.GameView.gameIdArgument.name}}",
            arguments = listOf(MenuRoute.GameView.gameIdArgument),
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments
                ?.getInt(MenuRoute.GameView.gameIdArgument.name)
                ?.run {
                    AwaitingGame.AwaitingGameId(this)
                }
            MapViewComponent(serverMapProvider)
        }
    }
}