package edu.agh.susgame.front.gui.components.menu.components.searchlobby

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.gui.components.common.theme.MenuBackground
import edu.agh.susgame.front.gui.components.common.theme.MenuButton
import edu.agh.susgame.front.gui.components.common.theme.RefreshIcon
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.menu.components.searchlobby.elements.HeaderLobby
import edu.agh.susgame.front.gui.components.menu.components.searchlobby.elements.LoadingAnim
import edu.agh.susgame.front.gui.components.menu.components.searchlobby.elements.LobbyRow
import edu.agh.susgame.front.gui.components.menu.components.searchlobby.elements.PinInput
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute
import edu.agh.susgame.front.service.interfaces.LobbyService


@Composable
fun SearchLobbiesView(
    lobbyService: LobbyService,
    navController: NavController
) {
    val awaitingGames = remember { mutableStateOf<Map<LobbyId, Lobby>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val currentLobbyIdWithPin: MutableState<LobbyId?> = remember { mutableStateOf(null) }
    val refreshGames: () -> Unit = {
        lobbyService
            .getAll()
            .thenAccept {
                awaitingGames.value = it
                isLoading = false
            }
    }

    LaunchedEffect(Unit) {
        refreshGames()
    }

    MenuBackground()
    MenuButton(
        text = Translation.Button.GO_BACK,
        onClick = { navController.navigate(MenuRoute.MainMenu.route) },
        alignment = Alignment.BottomStart
    )

    if (isLoading) {
        LoadingAnim()
        HeaderLobby(text = Translation.Lobby.FINDING_GAMES)

    } else {
        HeaderLobby(text = Translation.Lobby.CHOOSE_GAME)
        RefreshIcon(onRefreshClicked = refreshGames)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxHeight(0.6f),
                contentAlignment = Alignment.Center
            ) {
                if (currentLobbyIdWithPin.value == null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .fillMaxWidth(0.8f),
                    ) {
                        awaitingGames.value?.forEach {
                            LobbyRow(it.value, currentLobbyIdWithPin, navController)
                        }
                    }
                } else {
                    PinInput(currentLobbyIdWithPin, navController)
                }
            }
        }
    }
}
