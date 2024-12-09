package edu.agh.susgame.front.gui.components.menu.components.lobby

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.gui.components.common.theme.MenuBackground
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.LobbyComp
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.FailedToLoadComp
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.LobbyService


@Composable
fun LobbyView(
    lobbyId: LobbyId,
    lobbyService: LobbyService,
    gameService: GameService,
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
    MenuBackground()
    Column {
        when {
            isLoading -> Text(text = "${Translation.Button.LOADING}...")
            lobby == null -> FailedToLoadComp(lobbyId, navController)
            else -> lobby?.let {

                LobbyComp(it, lobbyService, gameService, navController)
            }
        }
    }
}