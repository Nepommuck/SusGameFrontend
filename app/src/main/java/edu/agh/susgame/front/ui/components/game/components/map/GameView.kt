package edu.agh.susgame.front.ui.components.game.components.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.Translation
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.providers.mock.MockServerMapProvider
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.ui.components.game.components.map.components.GameGraphComponent
import edu.agh.susgame.front.ui.graph.GameMapFront

@Composable
fun GameView(
    lobbyId: LobbyId,
    menuNavController: NavController,
    gameService: GameService
) {
    var gameMapFront by remember { mutableStateOf<GameMapFront?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // NEW INSTANCE OF gameMapFront SHOULD BE CREATED HERE FROM gameService
    // PARSER SHOULD CONNECT gameService AND gameMapFront
    val serverMapProvider = MockServerMapProvider()
    serverMapProvider.getServerMapState(lobbyId)
        .thenAccept {
            gameMapFront = it
            isLoading = false
        }

//    gameMapFront?.let { gameService.initGameFront(it) }
    Column {
        if (isLoading) {
            Text(text = "${Translation.Button.LOADING}...")
        } else {
            when (gameMapFront) {
                null -> {
                    Column {
                        Text(text = Translation.Error.UNEXPECTED_ERROR)

                        Button(onClick = {
                            menuNavController.navigate(MenuRoute.SearchLobby.route)
                        }) {
                            Text(text = Translation.Button.BACK_TO_MAIN_MENU)
                        }
                    }
                }

                else -> Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    gameMapFront?.let {
                        GameGraphComponent(
                            gameMapFront = it,
                            gameService = gameService
                        )
                    }
                }
            }
        }
    }
}
