package edu.agh.susgame.front.gui.components.menu.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.LobbyPin
import edu.agh.susgame.front.gui.components.game.GameView
import edu.agh.susgame.front.gui.components.menu.components.createlobby.CreateLobbyView
import edu.agh.susgame.front.gui.components.menu.components.enterpin.EnterPinView
import edu.agh.susgame.front.gui.components.menu.components.lobby.LobbyView
import edu.agh.susgame.front.gui.components.menu.components.mainmenu.MainMenuView
import edu.agh.susgame.front.gui.components.menu.components.searchlobby.SearchLobbiesView
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.LobbyService
import edu.agh.susgame.front.service.web.IpAddressProvider


@Composable
fun MenuNavigationHost(
    menuNavController: NavHostController,
    lobbyService: LobbyService,
    gameService: GameService,
    ipAddressProvider: IpAddressProvider,
) {
    NavHost(
        navController = menuNavController,
        startDestination = MenuRoute.MainMenu.route,
    ) {
        composable(route = MenuRoute.MainMenu.route) {
            MainMenuView(menuNavController, ipAddressProvider)
        }

        composable(route = MenuRoute.FindGame.route) {
            SearchLobbiesView(lobbyService, menuNavController)
        }

        composable(route = MenuRoute.CreateGame.route) {
            CreateLobbyView(lobbyService, menuNavController)
        }

        composable(
            route = MenuRoute.EnterPin.route,
            arguments = MenuRoute.EnterPin.arguments,
        ) { backStackEntry ->
            val lobbyId = backStackEntry.arguments
                ?.getInt(MenuRoute.EnterPin.gameIdArgument.name)
                ?.let { LobbyId(it) }

            if (lobbyId != null) {
                EnterPinView(lobbyId, lobbyService, menuNavController)
            } else {
                menuNavController.navigate(MenuRoute.FindGame.route)
            }
        }

        composable(
            route = MenuRoute.Lobby.route,
            arguments = MenuRoute.Lobby.arguments,
        ) { backStackEntry ->
            val lobbyId = backStackEntry.arguments
                ?.getInt(MenuRoute.Lobby.gameIdArgument.name)
                ?.let { LobbyId(it) }

            val lobbyPin = backStackEntry.arguments
                ?.getString(MenuRoute.Lobby.gamePinArgument.name)
                ?.let { LobbyPin(it) }

            if (lobbyId != null)
                LobbyView(lobbyId, lobbyPin, lobbyService, gameService, menuNavController)
            else
                menuNavController.navigate(MenuRoute.FindGame.route)
        }

        composable(
            MenuRoute.Game.route,
            arguments = MenuRoute.Game.arguments,
        ) { backStackEntry ->
            val lobbyId = backStackEntry.arguments
                ?.getInt(MenuRoute.Game.gameIdArgument.name)
                ?.run {
                    LobbyId(this)
                }

            if (lobbyId != null) lobbyService.lobbyManager?.let {
                GameView(
                    menuNavController,
                    gameService,
                    lobbyManager = it
                )
            }
        }
    }
}
