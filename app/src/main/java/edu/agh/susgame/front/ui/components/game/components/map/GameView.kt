package edu.agh.susgame.front.ui.components.game.components.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.model.graph.GameGraph
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.service.interfaces.ServerMapProvider
import edu.agh.susgame.front.Translation
import edu.agh.susgame.front.ui.components.game.components.map.components.GameGraphComponent

@Composable
fun GameView(
    lobbyId: LobbyId,
    serverMapProvider: ServerMapProvider,
    menuNavController: NavController,
) {
    var mapState by remember { mutableStateOf<GameGraph?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    serverMapProvider.getServerMapState(lobbyId)
        .thenAccept {
            mapState = it
            isLoading = false
        }

    Column {
        if (isLoading) {
            Text(text = "${Translation.Button.LOADING}...")
        } else {
            when (mapState) {
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
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    mapState?.let { GameGraphComponent(it, serverMapProvider) }
                }
            }
        }
    }
}
