package edu.agh.susgame.front.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import edu.agh.susgame.dto.rest.model.LobbyId


sealed class MenuRoute(
    internal val mainRoute: String,
    arguments: List<NamedNavArgument> = emptyList()
) {
    val route: String =
        when (arguments) {
            emptyList<NamedNavArgument>() ->
                mainRoute

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
    }

    data object Lobby : MenuRoute(
        "lobby",
        arguments = listOf(LobbyArguments.gameId),
    ) {
        val gameIdArgument = LobbyArguments.gameId

        fun routeWithArgument(lobbyId: LobbyId) = "${Lobby.mainRoute}/${lobbyId.value}"
    }

    data object Game : MenuRoute("game") {
        val gameIdArgument = navArgument("game_id") {
            type = NavType.IntType
            nullable = false
        }
    }
}
