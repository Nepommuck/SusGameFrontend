package edu.agh.susgame.front.ui.component.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.front.model.AwaitingGame
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider
import edu.agh.susgame.front.ui.component.common.Header
import edu.agh.susgame.front.ui.component.menu.navigation.MenuRoute
import edu.agh.susgame.front.ui.theme.PaddingL


@Composable
fun AwaitingGameComponent(
    gameId: AwaitingGame.AwaitingGameId?,
    awaitingGamesProvider: AwaitingGamesProvider,
    navController: NavController,
) {
    val awaitingGame = gameId?.let {
        awaitingGamesProvider.getById(it)
    }

    when (awaitingGame) {
        null -> {
            Text(
                "Error: Failed to get game with id: "
                        + (gameId?.value?.toString() ?: "unknown")
            )
        }

        else -> {
            Column(Modifier.padding(PaddingL)) {
                Header(awaitingGame.name)

                Text(text = awaitingGame.description, Modifier.padding(vertical = PaddingL))
                Text(
                    text = "${awaitingGame.playersWaiting} Players waiting",
                    Modifier.padding(vertical = PaddingL)
                )

                Button(onClick = {
                    navController.navigate("${MenuRoute.GameView.route}/${gameId.value}")
                }) {
                    Text("Join & Play")
                }
            }
        }
    }


}
