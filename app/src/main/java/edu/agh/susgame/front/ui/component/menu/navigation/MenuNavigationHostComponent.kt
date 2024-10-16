package edu.agh.susgame.front.ui.component.menu.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.LobbyService
import edu.agh.susgame.front.service.interfaces.ServerMapProvider
import edu.agh.susgame.front.ui.component.game.navigation.GameNavBarComponent
import edu.agh.susgame.front.ui.component.menu.CreateLobbyView
import edu.agh.susgame.front.ui.component.menu.LobbyView
import edu.agh.susgame.front.ui.component.menu.MainMenuView
import edu.agh.susgame.front.ui.component.menu.search.SearchLobbiesView

@Composable
fun MenuNavigationHostComponent(
    menuNavController: NavHostController,
    serverMapProvider: ServerMapProvider,
    lobbyService: LobbyService,
    gameService: GameService,
) {
    NavHost(
        navController = menuNavController,
        startDestination = MenuRoute.MainMenu.route,
    ) {
        composable(route = MenuRoute.MainMenu.route) {
            MainMenuView(menuNavController)
        }

        composable(route = MenuRoute.SearchLobby.route) {
            SearchLobbiesView(lobbyService, menuNavController)
        }

        composable(route = MenuRoute.CreateLobby.route) {
            CreateLobbyView(lobbyService, menuNavController)
        }

        composable(
            route = MenuRoute.Lobby.route,
            arguments = listOf(MenuRoute.Lobby.gameIdArgument),
        ) { backStackEntry ->
            val lobbyId = backStackEntry.arguments
                ?.getInt(MenuRoute.Lobby.gameIdArgument.name)
                ?.run {
                    LobbyId(this)
                }

            when (lobbyId) {
                null ->
                    SearchLobbiesView(lobbyService, menuNavController)

                else ->
                    LobbyView(lobbyId, lobbyService, gameService, menuNavController)
            }
        }

        composable(
            "${MenuRoute.Game.route}/{${MenuRoute.Game.gameIdArgument.name}}",
            arguments = listOf(MenuRoute.Game.gameIdArgument),
        ) { backStackEntry ->
            val lobbyId = backStackEntry.arguments
                ?.getInt(MenuRoute.Game.gameIdArgument.name)
                ?.run {
                    LobbyId(this)
                }
            GameNavBarComponent(lobbyId, menuNavController, serverMapProvider, gameService)
        }
    }
}
