package edu.agh.susgame.front.gui.component.menu.components.lobby.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import edu.agh.susgame.front.managers.LobbyManager
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.LobbyService
import edu.agh.susgame.front.gui.components.common.theme.Header
import edu.agh.susgame.front.gui.components.common.theme.PaddingL
import edu.agh.susgame.front.gui.components.common.theme.PaddingS
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.PlayerRow
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute
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
                    text = "${Translation.Lobby.nPlayersAwaiting(lobbyManager.getHowManyPlayersInLobby())}:",
                    Modifier.padding(vertical = PaddingS)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(PaddingL)
                        .background(Color.Cyan),
                ) {
                    items(lobbyManager.playersMap.toList()) { (id, player) ->
                        PlayerRow(
                            id = id,
                            player = player,
                            lobbyManager = lobbyManager,
                            gameService = gameService
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                if (!hasPlayerJoined) {
                    val isError = isNicknameError(currentNickname, playerNicknameInputValue)
                    if (isError) {
                        Text(
                            text = Translation.Lobby.NICKNAME_ERROR_MESSAGE,
                            color = Color.Red,
                        )
                    }
                    OutlinedTextField(
                        value = playerNicknameInputValue,
                        onValueChange = { newValue ->
                            playerNicknameInputValue = newValue
                            updateNicknameIfValid(newValue) { updatedNickname ->
                                currentNickname = updatedNickname
                            }
                        },
                        label = { Text(text = Translation.Lobby.CHOOSE_NICKNAME) },
                        isError = isError,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
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
                            gameService.sendStartGame()
//                            navController.navigate("${MenuRoute.Game.route}/${lobby.id.value}")
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


