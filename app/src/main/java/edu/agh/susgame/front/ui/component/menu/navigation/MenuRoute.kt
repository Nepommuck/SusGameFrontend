package edu.agh.susgame.front.ui.component.menu.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import edu.agh.susgame.front.model.game.GameId


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

    data object SearchGame : MenuRoute("search-game")

    data object CreateGame : MenuRoute("create-game")

    private data object AwaitingGameArguments {
        val gameId = navArgument("game_id") {
            type = NavType.IntType
            nullable = false
        }
    }

    data object AwaitingGame : MenuRoute(
        "awaiting-game",
        arguments = listOf(AwaitingGameArguments.gameId),
    ) {
        val gameIdArgument = AwaitingGameArguments.gameId

        fun routeWithArgument(gameId: GameId) = "${AwaitingGame.mainRoute}/${gameId.value}"
    }

    data object Game : MenuRoute("game") {
        val gameIdArgument = navArgument("game_id") {
            type = NavType.IntType
            nullable = false
        }
    }
}