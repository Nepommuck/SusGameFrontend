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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.front.model.PlayerNickname
import edu.agh.susgame.front.model.game.AwaitingGame
import edu.agh.susgame.front.model.game.GameId
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.component.common.Header
import edu.agh.susgame.front.ui.theme.PaddingS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private val PlayerNickname = PlayerNickname("The-player")

@Composable
private fun AwaitingGameContentComponent(
    awaitingGameInitialState: AwaitingGame,
    awaitingGamesProvider: AwaitingGamesProvider,
    navController: NavController,
) {
    var awaitingGame by remember { mutableStateOf(awaitingGameInitialState) }
    Column {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Header(title = awaitingGame.name)

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
            var isLeaveButtonLoading by remember { mutableStateOf(false) }
            Button(
                enabled = !isLeaveButtonLoading,
                onClick = {
                    isLeaveButtonLoading = true
                    awaitingGamesProvider.leave(awaitingGame.id, PlayerNickname)
                        .thenRun {
                            CoroutineScope(Dispatchers.Main).launch {
                                navController.navigate(MenuRoute.SearchGame.route)
                            }
                            isLeaveButtonLoading = false
                        }
                }
            ) {
                Text(
                    text = if (isLeaveButtonLoading) Translation.Button.LOADING
                    else Translation.Button.LEAVE
                )
            }

            if (awaitingGame.playersWaiting.contains(PlayerNickname)) {
                Button(onClick = {
                    navController.navigate("${MenuRoute.Game.route}/${awaitingGame.id.value}")
                }) {
                    Text(text = Translation.Button.PLAY)
                }
            } else {
                var isJoinButtonLoading by remember { mutableStateOf(false) }
                Button(
                    enabled = !isJoinButtonLoading,
                    onClick = {
                        isJoinButtonLoading = true
                        awaitingGamesProvider.join(awaitingGame.id, PlayerNickname)
                            .thenRun {
                                awaitingGamesProvider.getById(awaitingGame.id).thenAccept {
                                    if (it != null) {
                                        awaitingGame = it
                                    }
                                    isJoinButtonLoading = false
                                }
                            }
                    },
                ) {
                    Text(
                        text = if (isJoinButtonLoading) Translation.Button.LOADING
                        else Translation.Button.JOIN
                    )
                }
            }
        }
    }
}

@Composable
fun AwaitingGameView(
    gameId: GameId,
    awaitingGamesProvider: AwaitingGamesProvider,
    navController: NavController,
) {
    var awaitingGame by remember { mutableStateOf<AwaitingGame?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        awaitingGamesProvider.getById(gameId)
            .thenAccept {
                awaitingGame = it
                isLoading = false
            }
    }

    Column {
        if (isLoading) {
            Text(text = "${Translation.Button.LOADING}...")
        } else {
            when (awaitingGame) {
                null -> {
                    Column {
                        Text(text = Translation.Error.failedToLoadGame(gameId))

                        Button(onClick = {
                            navController.navigate(MenuRoute.SearchGame.route)
                        }) {
                            Text(text = Translation.Button.GO_BACK)
                        }
                    }
                }

                else -> {
                    awaitingGame?.let {
                        AwaitingGameContentComponent(
                            it,
                            awaitingGamesProvider,
                            navController,
                        )
                    }
                }
            }
        }
    }
}
