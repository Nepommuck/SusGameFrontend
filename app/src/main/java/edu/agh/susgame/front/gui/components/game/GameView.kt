package edu.agh.susgame.front.gui.components.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.mock.createCustomMapState
import edu.agh.susgame.front.gui.components.game.components.GameGraphComponent
import edu.agh.susgame.front.managers.GameManager

@Composable
fun GameView(
    lobbyId: LobbyId,
    menuNavController: NavController,
    gameService: GameService
) {
    val gameManager = remember { mutableStateOf<GameManager?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(lobbyId) {
        // TODO LOAD MAP FROM THE SERVER AND INIT GameManager
        gameManager.value = createCustomMapState()
        isLoading.value = false
    }

    Column {
        if (isLoading.value) {
            Text(text = "${Translation.Button.LOADING}...")
        } else {
            gameManager.value?.let {
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