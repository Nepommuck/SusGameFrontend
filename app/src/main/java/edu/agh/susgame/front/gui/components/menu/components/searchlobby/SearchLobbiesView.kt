package edu.agh.susgame.front.gui.components.menu.components.searchlobby

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.menu.components.searchlobby.elements.HeaderLobby
import edu.agh.susgame.front.gui.components.menu.components.searchlobby.elements.LoadingAnim
import edu.agh.susgame.front.gui.components.menu.components.searchlobby.elements.LobbyRow
import edu.agh.susgame.front.gui.components.common.theme.RefreshIcon
import edu.agh.susgame.front.gui.components.menu.components.searchlobby.elements.ReturnButton
import edu.agh.susgame.front.service.interfaces.LobbyService


@Composable
fun SearchLobbiesView(
    lobbyService: LobbyService,
    navController: NavController
) {
    val awaitingGames = remember { mutableStateOf<Map<LobbyId, Lobby>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
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

    if (isLoading) {
        LoadingAnim()
        HeaderLobby(text = Translation.Lobby.FINDING_GAMES)

    } else {
        RefreshIcon(
            onRefreshClicked = refreshGames
        )
        Column(
            modifier = Modifier.fillMaxHeight(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeaderLobby(text = Translation.Lobby.CHOOSE_GAME)

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(0.7f),
            ) {
                awaitingGames.value?.forEach {
                    LobbyRow(it.value, navController)
                }
            }
//            ReturnButton(navController = navController)

        }
        ReturnButton(navController = navController)
    }
}
