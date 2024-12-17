package edu.agh.susgame.front.gui.components.menu.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.LobbyPin


sealed class MenuRoute(
    internal val mainRoute: String,
    val arguments: List<NamedNavArgument> = emptyList(),
) {
    open val route: String =
        when (arguments) {
            emptyList<NamedNavArgument>() -> mainRoute

            else -> mainRoute + arguments.joinToString(
                prefix = "/",
                separator = "/",
            ) { "{${it.name}}" }
        }

    data object MainMenu : MenuRoute("main-menu")

    data object FindGame : MenuRoute("search-game")

    data object CreateGame : MenuRoute("create-game")

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
            "$mainRoute/${lobbyId.value}" +
                    lobbyPin?.let { pin -> "?${gamePinArgument.name}=${pin.value}" }.orEmpty()

        // handle optional `gamePin` argument
        override val route =
            "$mainRoute/{${gameIdArgument.name}}?${gamePinArgument.name}={${gamePinArgument.name}}"
    }

    data object EnterPin : MenuRoute(
        mainRoute = "join-lobby",
        arguments = listOf(LobbyArguments.gameId),
    ) {
        val gameIdArgument = LobbyArguments.gameId

        fun routeWithArgument(lobbyId: LobbyId) = "$mainRoute/${lobbyId.value}"
    }

    data object Game : MenuRoute(
        mainRoute = "game",
        arguments = listOf(LobbyArguments.gameId),
    ) {
        val gameIdArgument = LobbyArguments.gameId

        fun routeWithArgument(lobbyId: LobbyId) = "$mainRoute/${lobbyId.value}"
    }
}
