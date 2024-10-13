package edu.agh.susgame.front.ui.component.menu.components.lobby

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import edu.agh.susgame.front.model.game.Lobby
import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.LobbyService
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.component.menu.components.lobby.elements.LobbyComp

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
                        LobbyComp(
                            it,
                            lobbyService,
                            gameService,
                            navController,
                        )
                    }
                }
            }
        }
    }
}
