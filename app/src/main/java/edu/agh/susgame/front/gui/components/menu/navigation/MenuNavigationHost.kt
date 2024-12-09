package edu.agh.susgame.front.gui.components.menu.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.gui.components.game.GameView
import edu.agh.susgame.front.gui.components.menu.components.createlobby.CreateLobbyView
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

            // TODO Cleanup one day
            // Very useful for development purposes
//            ComputerComponent(
//                gameService, GameManager(
//                    nodesList = listOf(
//                        Server(
//                            id = NodeId(1), name = "", position = Coordinates(0, 0),
//                            packetsToWin = 100,
//                            packetsReceived = mutableIntStateOf(0)
//                        )
//                    ),
//                    edgesList = emptyList(),
//                    playersList = emptyList(),
//                    serverId = NodeId(1),
//                    mapSize = Coordinates(100, 100),
//                    localPlayerId = PlayerId(21),
//                    criticalBufferOverheatLevel = 10,
//                    packetsToWin = 5,
//                    gameService = gameService,
//                )
//            )
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
