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
import edu.agh.susgame.front.gui.components.common.util.PreventNavigationBack
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.game.components.GameGraphComponent
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.managers.LobbyManager
import edu.agh.susgame.front.service.interfaces.GameService

@Composable
fun GameView(
    menuNavController: NavController,
    gameService: GameService,
    lobbyManager: LobbyManager
) {
    val gameManager = remember { mutableStateOf<GameManager?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(lobbyManager.gameManager) {
        gameManager.value = lobbyManager.gameManager.value
        isLoading.value = false
    }

    PreventNavigationBack()

    Column {
        if (isLoading.value) {
            Text(text = "${Translation.Button.LOADING}...")
        } else {
            gameManager.value?.let {
                Box(modifier = Modifier.fillMaxSize()) {
                    GameGraphComponent(
                        gameManager = it,
                        gameService = gameService,
                        navController = menuNavController,
                    )
                }
            } ?: run {
                Column {
                    Text(text = Translation.Error.UNEXPECTED_ERROR)
                    Button(onClick = {
                        menuNavController.navigate(MenuRoute.FindGame.route)
                    }) {
                        Text(text = Translation.Button.BACK_TO_MAIN_MENU)
                    }
                }
            }
        }
    }
}