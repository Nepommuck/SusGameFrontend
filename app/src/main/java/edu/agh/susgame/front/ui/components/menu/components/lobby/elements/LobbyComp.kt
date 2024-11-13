package edu.agh.susgame.front.ui.component.menu.components.lobby.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.front.Translation
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.LobbyService
import edu.agh.susgame.front.ui.components.common.Header
import edu.agh.susgame.front.ui.components.common.managers.LobbyManager
import edu.agh.susgame.front.ui.components.common.managers.PlayerStatus
import edu.agh.susgame.front.ui.components.common.theme.PaddingL
import edu.agh.susgame.front.ui.components.common.theme.PaddingS
import edu.agh.susgame.front.ui.components.menu.components.lobby.elements.icons.PlayerStatusIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
internal fun LobbyComp(
    lobbyInitialState: Lobby,
    lobbyService: LobbyService,
    gameService: GameService,
    navController: NavController,
) {
    // TODO move logic and web communication to this class
    val lobbyManager by remember {
        mutableStateOf(LobbyManager())
    }

    LaunchedEffect(lobbyInitialState) {
        lobbyManager.updateFromRest(lobbyInitialState)
        lobbyService.initLobbyManager(lobbyManager)
        gameService.addLobbyManager(lobbyManager)
    }

    var lobby by remember { mutableStateOf(lobbyInitialState) }
    var hasPlayerJoined by remember {
        mutableStateOf(gameService.isPlayerInLobby(lobbyInitialState.id))
    }
    var playerNicknameInputValue by remember { mutableStateOf("") }
    var currentNickname: PlayerNickname? by remember { mutableStateOf(null) }

    Column(modifier = Modifier.padding(PaddingL)) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Header(title = lobby.name)

            Text(
                text = "${Translation.Lobby.nPlayersAwaiting(lobby.playersWaiting.size)}:",
                Modifier.padding(vertical = PaddingS)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingL)
            ) {
                lobbyManager.playersMap.forEach() { (_, player) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .padding(vertical = PaddingS)
                    ) {
                        Text(text = player.name.value)
                        PlayerStatusIcon(playerStatus = player.status)
                    }
                }
            }
        }


        if (!hasPlayerJoined) {
            if (isNicknameError(currentNickname, playerNicknameInputValue)) {
                Text(text = Translation.Lobby.NICKNAME_ERROR_MESSAGE, color = Color.Red)
            }

            OutlinedTextField(
                label = { Text(text = Translation.Lobby.CHOOSE_NICKNAME) },
                modifier = Modifier.fillMaxWidth(),
                value = playerNicknameInputValue,
                onValueChange = {
                    playerNicknameInputValue = it
                    updateNicknameIfValid(it) { updatedNickname ->
                        currentNickname = updatedNickname
                    }
                },
                isError = isNicknameError(currentNickname, playerNicknameInputValue),
                singleLine = true,
            )
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
                            gameService.leaveLobby()
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
                    lobbyManager.localId?.let {
                        lobbyManager.updatePlayerStatus(
                            it,
                            PlayerStatus.READY
                        )
                    }
//                    navController.navigate("${MenuRoute.Game.route}/${lobby.id.value}")
                }) {
                    Text(text = Translation.Button.PLAY)
                }
            } else {
                var isJoinButtonLoading by remember { mutableStateOf(false) }
                Button(
                    enabled = !isJoinButtonLoading && currentNickname != null,
                    onClick = {
                        isJoinButtonLoading = true
                        currentNickname?.let { nickname ->
                            gameService.joinLobby(lobby.id, nickname)
                                .thenRun {
                                    // TODO Await server socket response instead
                                    // Explicit wait, because otherwise server responds with a list
                                    // that doesn't contain the new player
                                    Thread.sleep(1000)
                                    lobbyService.getById(lobby.id).thenAccept {
                                        if (it != null) {
                                            lobby = it
                                            lobbyManager.updateFromRest(lobby)
                                        }
                                        isJoinButtonLoading = false
                                    }
                                    hasPlayerJoined = true
                                }

                        }
                        lobbyManager.updateFromRest(lobby)
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

fun isNicknameError(currentNickname: PlayerNickname?, playerNicknameInputValue: String) =
    currentNickname == null && playerNicknameInputValue.isNotEmpty()

fun updateNicknameIfValid(
    newNickname: String,
    onNicknameChanged: (PlayerNickname?) -> Unit
) {
    val trimmedNickname = newNickname.trim()
    onNicknameChanged(
        if (trimmedNickname.any { it.isWhitespace() }) {
            null
        } else {
            PlayerNickname(trimmedNickname)
        }
    )
}
