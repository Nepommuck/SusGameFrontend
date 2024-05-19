package edu.agh.susgame.front.ui.component.menu.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.agh.susgame.front.model.game.GameId
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider
import edu.agh.susgame.front.providers.interfaces.ServerMapProvider
import edu.agh.susgame.front.ui.component.game.navigation.GameNavBarComponent
import edu.agh.susgame.front.ui.component.menu.AwaitingGameView
import edu.agh.susgame.front.ui.component.menu.CreateGameView
import edu.agh.susgame.front.ui.component.menu.MainMenuView
import edu.agh.susgame.front.ui.component.menu.search.SearchGamesView

@Composable
fun MenuNavigationHostComponent(
    menuNavController: NavHostController,
    serverMapProvider: ServerMapProvider,
    awaitingGamesProvider: AwaitingGamesProvider,
) {
    NavHost(
        navController = menuNavController,
        startDestination = MenuRoute.MainMenu.route,
    ) {
        composable(route = MenuRoute.MainMenu.route) {
            MainMenuView(menuNavController)
        }

        composable(route = MenuRoute.SearchGame.route) {
            SearchGamesView(awaitingGamesProvider, menuNavController)
        }

        composable(route = MenuRoute.CreateGame.route) {
            CreateGameView(awaitingGamesProvider, menuNavController)
        }

        composable(
            route = MenuRoute.AwaitingGame.route,
            arguments = listOf(MenuRoute.AwaitingGame.gameIdArgument),
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments
                ?.getInt(MenuRoute.AwaitingGame.gameIdArgument.name)
                ?.run {
                    GameId(this)
                }

            when (gameId) {
                null ->
                    SearchGamesView(awaitingGamesProvider, menuNavController)

                else ->
                    AwaitingGameView(gameId, awaitingGamesProvider, menuNavController)
            }
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
            GameNavBarComponent(gameId, menuNavController, serverMapProvider)
        }
    }
}
