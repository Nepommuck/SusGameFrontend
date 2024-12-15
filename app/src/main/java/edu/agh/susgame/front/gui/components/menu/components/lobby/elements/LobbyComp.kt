package edu.agh.susgame.front.gui.components.menu.components.lobby.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.agh.susgame.dto.rest.model.LobbyDetails
import edu.agh.susgame.dto.rest.model.LobbyPin
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.front.gui.components.common.theme.Header
import edu.agh.susgame.front.gui.components.common.theme.PaddingL
import edu.agh.susgame.front.gui.components.common.theme.PaddingS
import edu.agh.susgame.front.gui.components.common.theme.PaddingXL
import edu.agh.susgame.front.gui.components.common.theme.RefreshIcon
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.ColorMenuComp
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.PlayerRow
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.buttons.JoinButton
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.buttons.LeaveButton
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.buttons.StartButton
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute
import edu.agh.susgame.front.managers.LobbyManager
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.LobbyService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LobbyComp(
    lobbyDetails: LobbyDetails,
    lobbyPin: LobbyPin?,
    lobbyService: LobbyService,
    gameService: GameService,
    navController: NavController,
) {

    val lobbyManager by remember {
        mutableStateOf(
            LobbyManager(
                lobbyService = lobbyService,
                gameService = gameService,
                lobbyId = lobbyDetails.id,
                lobbyPin = lobbyPin,
                name = lobbyDetails.name,
                maxNumOfPlayers = lobbyDetails.maxNumOfPlayers,
            )
        )
    }

    var playerNicknameInputValue by remember { mutableStateOf("") }
    var currentNickname: PlayerNickname? by remember { mutableStateOf(null) }
    val isLeaveButtonLoading = remember { mutableStateOf(false) }
    val isJoinButtonLoading = remember { mutableStateOf(false) }

    val lobbyState = lobbyManager.lobbyState
    val isGameReady by remember { lobbyState.isGameMapReady }
    val isColorBeingChanged by remember { lobbyState.isColorBeingChanged }

    LaunchedEffect(lobbyManager) {
        lobbyManager.updateFromRest()
        lobbyService.addLobbyManager(lobbyManager)
        gameService.initLobbyManager(lobbyManager)
    }

    if (isGameReady) {
        navController.navigate(
            MenuRoute.Game.routeWithArgument(lobbyId = lobbyManager.lobbyId)
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        lobbyPin?.let {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingXL),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(text = "${Translation.Lobby.PIN}: ${it.value}", style = TextStyler.TerminalM)
            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.1f))
            Header(title = lobbyManager.name, style = TextStyler.TerminalXL)
            Spacer(modifier = Modifier.weight(0.1f))

            Row(modifier = Modifier.weight(2f)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1.2f)
                        .padding(PaddingL),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth(0.5f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${Translation.Lobby.NUM_OF_PLAYERS}: ${lobbyManager.getNumberOfPlayers()}/${lobbyManager.maxNumOfPlayers}",
                            style = TextStyler.TerminalM
                        )
                        RefreshIcon(onRefreshClicked = { lobbyManager.updateFromRest() })
                    }

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(PaddingS),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        items(lobbyManager.playersMap.toList()) { (_, player) ->
                            PlayerRow(
                                player = player,
                                lobbyManager = lobbyManager,
                            )
                        }
                    }
                    Box(modifier = Modifier.weight(0.2f)) {
                        LeaveButton(onClick = {
                            handleLeaveButtonClick(
                                navController,
                                lobbyManager,
                                isLeaveButtonLoading
                            )
                        })
                    }
                }


                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(PaddingXL),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isColorBeingChanged) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.3f))
                        ) {
                            ColorMenuComp(
                                onColorSelected = { newColor ->
                                    lobbyManager.handlePlayerColorChange(newColor)
                                })
                        }
                    }
                    if (!lobbyState.hasPlayerJoined.value) {
                        val isError = isNicknameError(currentNickname, playerNicknameInputValue)
                        if (isError) {
                            Text(
                                text = Translation.Lobby.NICKNAME_ERROR_MESSAGE,
                                color = Color.Red,
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            OutlinedTextField(
                                value = playerNicknameInputValue,
                                onValueChange = { newValue ->
                                    playerNicknameInputValue = newValue
                                    updateNicknameOrInvalidate(newValue) { validatedNicknameOrNull ->
                                        currentNickname = validatedNicknameOrNull
                                    }
                                },
                                label = {
                                    Text(
                                        text = Translation.Lobby.CHOOSE_NICKNAME,
                                        style = TextStyler.TerminalS
                                    )
                                },
                                isError = isError,
                                singleLine = true,
                                textStyle = TextStyler.TerminalM.copy(textAlign = TextAlign.Center),
                                modifier = Modifier
                                    .weight(3f)
                                    .padding(PaddingS)
                            )

                            JoinButton(
                                isJoinButtonLoading = isJoinButtonLoading.value,
                                isEnabled = currentNickname != null,
                                onClick = {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        handleJoinButtonClick(
                                            currentNickname = currentNickname,
                                            lobbyManager = lobbyManager,
                                            isJoinButtonLoading = isJoinButtonLoading,
                                        )
                                    }
                                }
                            )
                        }
                    }
                    if (lobbyState.hasPlayerJoined.value) {
                        StartButton(
                            isEnabled = lobbyState.areAllPlayersReady.value && lobbyManager.getNumberOfPlayers() >= 2,
                            onClick = {
                                if (!isGameReady) {
                                    gameService.sendStartGame()
                                }
                            }
                        )
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
    isLeaveButtonLoading: MutableState<Boolean>,
) {
    isLeaveButtonLoading.value = true

    if (!lobbyManager.lobbyState.hasPlayerJoined.value) {
        CoroutineScope(Dispatchers.Main).launch {
            navController.navigate(MenuRoute.FindGame.route)
        }
    } else {
        lobbyManager.handleLocalPlayerLeave()

        CoroutineScope(Dispatchers.Main).launch {
            navController.navigate(MenuRoute.FindGame.route)
        }
        isLeaveButtonLoading.value = false
    }
}

fun handleJoinButtonClick(
    currentNickname: PlayerNickname?,
    lobbyManager: LobbyManager,
    isJoinButtonLoading: MutableState<Boolean>,
) {
    isJoinButtonLoading.value = true

    currentNickname?.let { nickname ->
        lobbyManager.handleLocalPlayerJoin(nickname)
        isJoinButtonLoading.value = false
    }
}
