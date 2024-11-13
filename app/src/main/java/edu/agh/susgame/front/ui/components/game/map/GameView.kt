package edu.agh.susgame.front.ui.components.game.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.Translation
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.mock.createCustomMapState
import edu.agh.susgame.front.ui.components.game.map.components.GameGraphComponent
import edu.agh.susgame.front.ui.components.common.managers.GameManager

@Composable
fun GameView(
    lobbyId: LobbyId,
    menuNavController: NavController,
    gameService: GameService
) {
    // TODO Make it val
    var gameManager by remember { mutableStateOf<GameManager?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(lobbyId) {
        // TODO LOAD MAP FROM THE SERVER AND INIT GameManager
        gameManager = createCustomMapState()
        isLoading = false
    }

    Column {
        if (isLoading) {
            Text(text = "${Translation.Button.LOADING}...")
        } else {
            gameManager?.let {
                Box(modifier = Modifier.fillMaxSize()) {
                    GameGraphComponent(gameManager = it, gameService = gameService)
                }
            } ?: run {
                Column {
                    Text(text = Translation.Error.UNEXPECTED_ERROR)
                    Button(onClick = {
                        menuNavController.navigate(MenuRoute.SearchLobby.route)
                    }) {
                        Text(text = Translation.Button.BACK_TO_MAIN_MENU)
                    }
                }
            }
        }
    }
}