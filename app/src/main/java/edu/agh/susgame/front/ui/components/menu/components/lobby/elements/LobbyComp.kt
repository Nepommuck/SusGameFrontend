package edu.agh.susgame.front.ui.component.menu.components.lobby.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.front.ui.components.common.util.Translation
import edu.agh.susgame.front.ui.components.common.navigation.MenuRoute
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.LobbyService
import edu.agh.susgame.front.ui.components.common.theme.Header
import edu.agh.susgame.front.managers.LobbyManager
import edu.agh.susgame.front.ui.components.common.theme.PaddingL
import edu.agh.susgame.front.ui.components.common.theme.PaddingS
import edu.agh.susgame.front.ui.components.common.util.player.PlayerStatus
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
        lobbyService.addLobbyManager(lobbyManager)
        gameService.addLobbyManager(lobbyManager)
    }

    var lobby by remember { mutableStateOf(lobbyInitialState) }

    var hasPlayerJoined by remember {
        mutableStateOf(gameService.isPlayerInLobby(lobbyInitialState.id))
    }

    var playerNicknameInputValue by remember { mutableStateOf("") }
    var currentNickname: PlayerNickname? by remember { mutableStateOf(null) }
    var isLeaveButtonLoading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(PaddingL)) {
        Header(title = lobby.name)
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "${Translation.Lobby.nPlayersAwaiting(lobby.playersWaiting.size)}:",
                    Modifier.padding(vertical = PaddingS)
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(PaddingL)
                        .background(Color.Cyan),
                ) {
                    println(lobbyManager.playersMap.size)
                    lobbyManager.playersMap.forEach { (id, player) ->
                        Row(
                            modifier = Modifier
                                .padding(PaddingS)
                                .fillMaxWidth()
                                .height(40.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(4f)
                                    .background(Color.Gray)
                                    .fillMaxSize()
                                    .align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    text = player.name.value
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(Color.DarkGray)
                                    .fillMaxSize()
                                    .align(Alignment.CenterVertically)
                                    .let {
                                        if (id == lobbyManager.localId) it.clickable {
                                            lobbyManager.localId?.let { id ->
                                                gameService.sendChangingStateRequest(
                                                    id,
                                                    PlayerStatus.READY
                                                )

                                            } // SOCKET
                                            lobbyManager.updatePlayerStatus(lobbyManager.localId!!,PlayerStatus.READY)
                                        }
                                        else it
                                    }
                            ) {
                                PlayerStatusIcon(

                                    playerStatus = player.status
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
//                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                if (!hasPlayerJoined) {
                    if (isNicknameError(currentNickname, playerNicknameInputValue)) {
                        Text(text = Translation.Lobby.NICKNAME_ERROR_MESSAGE, color = Color.Red)
                    }

                    OutlinedTextField(
                        label = { Text(text = Translation.Lobby.CHOOSE_NICKNAME) },
                        modifier = Modifier,
//                            .fillMaxWidth(),
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
//                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(
                        enabled = !isLeaveButtonLoading,
                        onClick = {
                            handleLeaveButtonClick(
                                navController = navController,
                                lobbyManager = lobbyManager,
                                gameService = gameService,
                                hasPlayerJoined = hasPlayerJoined,
                                setLeaveButtonLoading = { isLeaveButtonLoading = it },
                                setHasPlayerJoined = { hasPlayerJoined = it }
                            )
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
                                gameService.sendChangingStateRequest(
                                    it,
                                    PlayerStatus.READY
                                )
                            } // SOCKET
//                    navController.navigate("${MenuRoute.Game.route}/${lobby.id.value}")
                        }) {
                            Text(text = Translation.Button.PLAY)
                        }
                    } else {
                        var isJoinButtonLoading by remember { mutableStateOf(false) }
                        Button(
                            enabled = !isJoinButtonLoading && currentNickname != null,
                            onClick = {
                                handleJoinButtonClick(
                                    lobby = lobby,
                                    currentNickname = currentNickname,
                                    gameService = gameService,
                                    lobbyService = lobbyService,
                                    lobbyManager = lobbyManager,
                                    setJoinButtonLoading = { isJoinButtonLoading = it },
                                    setLobby = { lobby = it },
                                    setHasPlayerJoined = { hasPlayerJoined = it }
                                )
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

fun handleLeaveButtonClick(
    navController: NavController,
    lobbyManager: LobbyManager,
    gameService: GameService,
    hasPlayerJoined: Boolean,
    setLeaveButtonLoading: (Boolean) -> Unit,
    setHasPlayerJoined: (Boolean) -> Unit
) {
    setLeaveButtonLoading(true)

    if (!hasPlayerJoined) {
        CoroutineScope(Dispatchers.Main).launch {
            navController.navigate(MenuRoute.SearchLobby.route)
        }
    } else {
        lobbyManager.localId?.let { gameService.sendLeavingRequest(it) } // SOCKET

        gameService.leaveLobby()
            .thenRun {
                setHasPlayerJoined(false)
                CoroutineScope(Dispatchers.Main).launch {
                    navController.navigate(MenuRoute.SearchLobby.route)
                }
                setLeaveButtonLoading(false)
            }
    }
}

fun handleJoinButtonClick(
    lobby: Lobby,
    currentNickname: PlayerNickname?,
    gameService: GameService,
    lobbyService: LobbyService,
    lobbyManager: LobbyManager,
    setJoinButtonLoading: (Boolean) -> Unit,
    setLobby: (Lobby) -> Unit,
    setHasPlayerJoined: (Boolean) -> Unit
) {
    setJoinButtonLoading(true)

    currentNickname?.let { nickname ->
        gameService.joinLobby(lobby.id, nickname).thenRun {

            // TODO: Await server socket response instead
            // Explicit wait to ensure server updates with new player info
            Thread.sleep(1000)
            lobbyService.getById(lobby.id).thenAccept { updatedLobby ->
                if (updatedLobby != null) {
                    setLobby(updatedLobby)
                    lobbyManager.updateFromRest(updatedLobby)
                }
                setJoinButtonLoading(false)
            }
            setHasPlayerJoined(true)
        }

        gameService.sendJoiningRequest(nickname) // SOCKET
    }

}


