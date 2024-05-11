package edu.agh.susgame.front.ui.component.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.front.model.PlayerNickname
import edu.agh.susgame.front.model.game.AwaitingGame
import edu.agh.susgame.front.model.game.GameId
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.component.common.Header
import edu.agh.susgame.front.ui.component.menu.navigation.MenuRoute
import edu.agh.susgame.front.ui.theme.PaddingL
import edu.agh.susgame.front.ui.theme.PaddingS


private val PlayerNickname = PlayerNickname("The-player")

@Composable
private fun AwaitingGameContentComponent(
    awaitingGame: AwaitingGame,
    awaitingGamesProvider: AwaitingGamesProvider,
    navController: NavController,
) {
    Column(Modifier.padding(PaddingL)) {
        Header(awaitingGame.name)

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Text(
                text = "${Translation.Menu.SearchGame.nPlayersAwaiting(awaitingGame.playersWaiting.size)}:",
                Modifier.padding(vertical = PaddingS)
            )
            awaitingGame.playersWaiting.forEach {
                Text(text = it.value)
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(onClick = {
                awaitingGamesProvider.leave(awaitingGame.id, PlayerNickname)
                navController.navigate(MenuRoute.SearchGame.route)
            }) {
                Text(text = Translation.Button.LEAVE)
            }

            if (awaitingGame.playersWaiting.contains(PlayerNickname)) {
                Button(onClick = {
                    navController.navigate("${MenuRoute.Game.route}/${awaitingGame.id.value}")
                }) {
                    Text(text = Translation.Button.PLAY)
                }
            } else {
                Button(onClick = {
                    awaitingGamesProvider.join(awaitingGame.id, PlayerNickname)
                    navController.navigate("${MenuRoute.AwaitingGame.route}/${awaitingGame.id.value}")
                }) {
                    Text(text = Translation.Button.JOIN)
                }
            }
        }
    }
}

@Composable
fun AwaitingGameView(
    gameId: GameId?,
    awaitingGamesProvider: AwaitingGamesProvider,
    navController: NavController,
) {
    val awaitingGame = gameId?.let {
        awaitingGamesProvider.getById(it)
    }

    when (awaitingGame) {
        null -> {
            Column {
                Text(
                    "Error: Failed to get game with id: "
                            + (gameId?.value?.toString() ?: "unknown")
                )
                Button(onClick = {
                    navController.navigate(MenuRoute.SearchGame.route)
                }) {
                    Text(text = "Go back")
                }
            }
        }

        else -> {
            AwaitingGameContentComponent(
                awaitingGame,
                awaitingGamesProvider,
                navController,
            )
        }
    }
}
