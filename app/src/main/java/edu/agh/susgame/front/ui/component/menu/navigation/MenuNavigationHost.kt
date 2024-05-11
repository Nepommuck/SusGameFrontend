package edu.agh.susgame.front.ui.component.menu.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.agh.susgame.front.model.game.GameId
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider
import edu.agh.susgame.front.providers.interfaces.ServerMapProvider
import edu.agh.susgame.front.ui.component.game.navigation.GameNavBar
import edu.agh.susgame.front.ui.component.menu.AwaitingGameView
import edu.agh.susgame.front.ui.component.menu.CreateGameView
import edu.agh.susgame.front.ui.component.menu.MainMenuView
import edu.agh.susgame.front.ui.component.menu.search.SearchGamesView

@Composable
fun MenuNavigationHost(
    navController: NavHostController,
    serverMapProvider: ServerMapProvider,
    awaitingGamesProvider: AwaitingGamesProvider,
) {
    NavHost(
        navController = navController,
        startDestination = MenuRoute.MainMenu.route,
    ) {
        composable(MenuRoute.MainMenu.route) {
            MainMenuView(navController)
        }
        composable(MenuRoute.SearchGame.route) {
            SearchGamesView(awaitingGamesProvider, navController)
        }
        composable(MenuRoute.CreateGame.route) {
            CreateGameView(navController)
        }
        composable(
            "${MenuRoute.AwaitingGame.route}/{${MenuRoute.AwaitingGame.gameIdArgument.name}}",
            arguments = listOf(MenuRoute.AwaitingGame.gameIdArgument),
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments
                ?.getInt(MenuRoute.AwaitingGame.gameIdArgument.name)
                ?.run {
                    GameId(this)
                }
            AwaitingGameView(gameId, awaitingGamesProvider, navController)
        }
        composable(
            "${MenuRoute.Game.route}/{${MenuRoute.Game.gameIdArgument.name}}",
            arguments = listOf(MenuRoute.Game.gameIdArgument),
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments
                ?.getInt(MenuRoute.Game.gameIdArgument.name)
                ?.run {
                    GameId(this)
                }
            GameNavBar(gameId, serverMapProvider)
        }
    }
}