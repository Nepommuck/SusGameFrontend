package edu.agh.susgame.front.ui.components.game.components.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.model.graph.GameGraph
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.service.interfaces.ServerMapProvider
import edu.agh.susgame.front.Translation
import edu.agh.susgame.front.model.graph.nodes.Server
import edu.agh.susgame.front.ui.components.game.components.map.components.GameGraphComponent
import edu.agh.susgame.front.ui.components.game.components.map.components.elements.ProgressBarComp

@Composable
fun GameView(
    lobbyId: LobbyId,
    serverMapProvider: ServerMapProvider,
    menuNavController: NavController,
) {
    var gameGraph by remember { mutableStateOf<GameGraph?>(null) }
    var isLoading by remember { mutableStateOf(true) }


    serverMapProvider.getServerMapState(lobbyId)
        .thenAccept {
            gameGraph = it
            isLoading = false
        }

    Column {
        if (isLoading) {
            Text(text = "${Translation.Button.LOADING}...")
        } else {
            when (gameGraph) {
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
//                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {

                    gameGraph?.let { GameGraphComponent(it, serverMapProvider) }
                }
            }
        }
    }
}