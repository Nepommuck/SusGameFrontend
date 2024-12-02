package edu.agh.susgame.front.gui.component.menu.components.lobby.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import edu.agh.susgame.front.gui.components.common.theme.Header
import edu.agh.susgame.front.gui.components.common.theme.PaddingL
import edu.agh.susgame.front.gui.components.common.theme.PaddingS
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.ColorMenuComp
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.PlayerRow
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute
import edu.agh.susgame.front.managers.LobbyManager
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.LobbyService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
internal fun LobbyComp(
    lobbyInit: Lobby,
    lobbyService: LobbyService,
    gameService: GameService,
    navController: NavController,
) {

    val lobbyManager by remember {
        mutableStateOf(
            LobbyManager(
                lobbyService = lobbyService,
                id = lobbyInit.id,
                name = lobbyInit.name,
//                maxNumOfPlayers = lobbyInit.maxNumOfPlayers,
//                gameTime = lobbyInit.gameTime
            )
        )
    }

    var hasPlayerJoined by remember {
        mutableStateOf(gameService.isPlayerInLobby(lobbyInit.id))
    }
    var playerNicknameInputValue by remember { mutableStateOf("") }
    var currentNickname: PlayerNickname? by remember { mutableStateOf(null) }
    var isLeaveButtonLoading by remember { mutableStateOf(false) }
    val isGameReady by lobbyManager.isGameReady
    val isColorBeingChanged by lobbyManager.isColorBeingChanged

    LaunchedEffect(lobbyManager) {
        lobbyManager.updateFromRest(lobbyInit)
        lobbyService.addLobbyManager(lobbyManager)
        gameService.initLobbyManager(lobbyManager)
    }

    Column(modifier = Modifier.padding(PaddingL)) {
        Header(title = lobbyManager.name)
        Row {

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "${Translation.Lobby.nPlayersAwaiting(lobbyManager.countPlayers())}:",
                    Modifier.padding(vertical = PaddingS)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(PaddingL),
                ) {
                    items(lobbyManager.playersMap.toList()) { (id, player) ->
                        PlayerRow(
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
                if (isColorBeingChanged) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray)) {
                        ColorMenuComp(
                            onColorSelected = { newColor ->
                                lobbyManager.setPlayerColor(lobbyManager.localPlayer.id, newColor)
                                gameService.sendPlayerChangeColor(lobbyManager.localPlayer.id, newColor.value)
                                lobbyManager.isColorBeingChanged.value = false
                            })
                    }
                }
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
                            updateNicknameOrInvalidate(newValue) { validatedNicknameOrNull ->
                                currentNickname = validatedNicknameOrNull
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
                            if (!isGameReady) {
                                gameService.sendStartGame()
                            } else {
                                navController.navigate("${MenuRoute.Game.route}/${lobbyManager.id.value}")
                            }

                        }) {
                            if (!isGameReady) {
                                Text(text = Translation.Button.PLAY)
                            } else {
                                Text(text = Translation.Button.MOVE_TO_GAME)
                            }
                        }
                    } else {
                        var isJoinButtonLoading by remember { mutableStateOf(false) }
                        Button(
                            enabled = !isJoinButtonLoading && currentNickname != null,
                            onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    handleJoinButtonClick(
                                        currentNickname = currentNickname,
                                        gameService = gameService,
                                        lobbyManager = lobbyManager,
                                        setJoinButtonLoading = { isJoinButtonLoading = it },
                                        setHasPlayerJoined = { hasPlayerJoined = it }
                                    )
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
    }
}


fun isNicknameError(currentNickname: PlayerNickname?, playerNicknameInputValue: String) =
    currentNickname == null && playerNicknameInputValue.isNotEmpty()

fun updateNicknameOrInvalidate(
    newNicknameInputValue: String,
    onNicknameChanged: (PlayerNickname?) -> Unit
) {
    val trimmedNickname = newNicknameInputValue.trim()
    onNicknameChanged(
        if (trimmedNickname.any { it.isWhitespace() }) null else PlayerNickname(trimmedNickname)
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
        gameService.sendLeavingRequest(lobbyManager.localPlayer.id)
        gameService.leaveLobby().thenRun {
            setHasPlayerJoined(false)
            CoroutineScope(Dispatchers.Main).launch {
                navController.navigate(MenuRoute.SearchLobby.route)
            }
            setLeaveButtonLoading(false)
        }
    }
}

fun handleJoinButtonClick(
    currentNickname: PlayerNickname?,
    gameService: GameService,
    lobbyManager: LobbyManager,
    setJoinButtonLoading: (Boolean) -> Unit,
    setHasPlayerJoined: (Boolean) -> Unit
) {
    setJoinButtonLoading(true)
    currentNickname?.let { nickname ->
        lobbyManager.localPlayer.name = currentNickname
        gameService.joinLobby(lobbyManager.id, nickname).thenRun {
            setJoinButtonLoading(false)
            setHasPlayerJoined(true)
        }
    }
}