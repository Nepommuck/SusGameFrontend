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
import edu.agh.susgame.front.model.Player
import edu.agh.susgame.front.model.PlayerNickname
import edu.agh.susgame.front.model.game.Lobby
import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.LobbyService
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.component.common.Header
import edu.agh.susgame.front.ui.theme.PaddingS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private val player: Player = Player(nickname = PlayerNickname("The-player"))

@Composable
private fun LobbyContentComponent(
    lobbyInitialState: Lobby,
    lobbyService: LobbyService,
    webGameService: GameService,
    navController: NavController,
) {
    var lobby by remember { mutableStateOf(lobbyInitialState) }
    var hasPlayerJoined by remember {
        mutableStateOf(webGameService.isPlayerInLobby(lobbyInitialState.id))
    }

    Column {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Header(title = lobby.name)

            Text(
                text = "${Translation.Menu.SearchGame.nPlayersAwaiting(lobby.playersWaiting.size)}:",
                Modifier.padding(vertical = PaddingS)
            )
            lobby.playersWaiting.values.forEach {
                Text(text = it.nickname.value)
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

                    when (hasPlayerJoined) {
                        false -> {
                            CoroutineScope(Dispatchers.Main).launch {
                                navController.navigate(MenuRoute.SearchLobby.route)
                            }
                        }

                        true -> {
                            webGameService.leaveLobby()
                                .thenRun {
                                    hasPlayerJoined = false
                                    CoroutineScope(Dispatchers.Main).launch {
                                        navController.navigate(MenuRoute.SearchLobby.route)
                                    }
                                    isLeaveButtonLoading = false
                                }
                        }
                    }
                }
            ) {
                Text(
                    text = if (isLeaveButtonLoading) Translation.Button.LOADING
                    else Translation.Button.LEAVE
                )
            }

            if (hasPlayerJoined) {
                Button(onClick = {
                    navController.navigate("${MenuRoute.Game.route}/${lobby.id.value}")
                }) {
                    Text(text = Translation.Button.PLAY)
                }
            } else {
                var isJoinButtonLoading by remember { mutableStateOf(false) }
                Button(
                    enabled = !isJoinButtonLoading,
                    onClick = {
                        isJoinButtonLoading = true
                        webGameService.joinLobby(lobby.id, player.nickname)
                            .thenRun {
                                // TODO Await server socket response instead
                                // Explicit wait, because otherwise server responds with a list
                                // that doesn't contain the new player
                                Thread.sleep(500)
                                lobbyService.getById(lobby.id).thenAccept {
                                    if (it != null) {
                                        lobby = it
                                    }
                                    isJoinButtonLoading = false
                                }
                                hasPlayerJoined = true
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
fun LobbyView(
    lobbyId: LobbyId,
    lobbyService: LobbyService,
    webGameService: GameService,
    navController: NavController,
) {
    var lobby by remember { mutableStateOf<Lobby?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        lobbyService.getById(lobbyId)
            .thenAccept {
                lobby = it
                isLoading = false
            }
    }

    Column {
        if (isLoading) {
            Text(text = "${Translation.Button.LOADING}...")
        } else {
            when (lobby) {
                null -> {
                    Column {
                        Text(text = Translation.Error.failedToLoadGame(lobbyId))

                        Button(onClick = {
                            navController.navigate(MenuRoute.SearchLobby.route)
                        }) {
                            Text(text = Translation.Button.GO_BACK)
                        }
                    }
                }

                else -> {
                    lobby?.let {
                        LobbyContentComponent(
                            it,
                            lobbyService,
                            webGameService,
                            navController,
                        )
                    }
                }
            }
        }
    }
}
