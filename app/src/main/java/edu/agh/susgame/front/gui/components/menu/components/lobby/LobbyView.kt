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
import edu.agh.susgame.dto.rest.model.LobbyDetails
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.LobbyPin
import edu.agh.susgame.front.gui.components.common.theme.MenuBackground
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.LobbyComp
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.FailedToLoadComp
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.GetGameDetailsResult
import edu.agh.susgame.front.service.interfaces.LobbyService


@Composable
fun LobbyView(
    lobbyId: LobbyId,
    lobbyPin: LobbyPin?,
    lobbyService: LobbyService,
    gameService: GameService,
    navController: NavController,
) {
    var lobbyDetails by remember { mutableStateOf<LobbyDetails?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        lobbyService.getLobbyDetails(lobbyId, lobbyPin)
            .thenAccept { result ->
                if (result is GetGameDetailsResult.Success) {
                    lobbyDetails = result.lobbyDetails
                    isLoading = false
                } else {
                    // TODO GAME-121
                    println("Wrong PIN")
                }
            }
    }

    MenuBackground()
    Column {
        when {
            isLoading ->
                Text(text = "${Translation.Button.LOADING}...")

            lobbyDetails == null ->
                FailedToLoadComp(lobbyId, navController)

            else -> lobbyDetails?.let { details ->
                LobbyComp(details, lobbyPin, lobbyService, gameService, navController)
            }
        }
    }
}
