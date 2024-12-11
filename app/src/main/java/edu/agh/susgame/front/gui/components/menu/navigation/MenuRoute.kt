package edu.agh.susgame.front.gui.components.menu.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.LobbyPin


sealed class MenuRoute(
    internal val mainRoute: String,
    arguments: List<NamedNavArgument> = emptyList()
) {
    val route: String =
        when (arguments) {
            emptyList<NamedNavArgument>() -> mainRoute

            else -> mainRoute + arguments.joinToString(
                prefix = "/",
                separator = "/",
            ) { "{${it.name}}" }
        }

    data object MainMenu : MenuRoute("main-menu")

    data object SearchLobby : MenuRoute("search-game")

    data object CreateLobby : MenuRoute("create-game")

    private data object LobbyArguments {
        val gameId = navArgument("game_id") {
            type = NavType.IntType
            nullable = false
        }
        val gamePin = navArgument("pin") {
            type = NavType.StringType
            nullable = true
        }
    }

    data object Lobby : MenuRoute(
        mainRoute = "lobby",
        arguments = listOf(LobbyArguments.gameId, LobbyArguments.gamePin),
    ) {
        val gameIdArgument = LobbyArguments.gameId
        val gamePinArgument = LobbyArguments.gamePin

        fun routeWithArgument(lobbyId: LobbyId, lobbyPin: LobbyPin? = null) =
            "${Lobby.mainRoute}/${lobbyId.value}" +
                    lobbyPin?.let { pin -> "?${gamePinArgument.name}=${pin.value}" }.orEmpty()
    }

    data object EnterPin : MenuRoute(
        mainRoute = "join-lobby",
        arguments = listOf(LobbyArguments.gameId),
    ) {
        val gameIdArgument = LobbyArguments.gameId

        fun routeWithArgument(lobbyId: LobbyId) = "${EnterPin.mainRoute}/${lobbyId.value}"
    }

    data object Game : MenuRoute("game") {
        val gameIdArgument = LobbyArguments.gameId
    }
}
