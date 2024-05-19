package edu.agh.susgame.front.ui.component.game.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
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
import edu.agh.susgame.front.model.ServerMapState
import edu.agh.susgame.front.model.game.GameId
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.providers.interfaces.ServerMapProvider
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.theme.PaddingL

@Composable
fun ServerMapView(
    gameId: GameId,
    serverMapProvider: ServerMapProvider,
    menuNavController: NavController,
) {
    var mapState by remember { mutableStateOf<ServerMapState?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    serverMapProvider.getServerMapState(gameId)
        .thenAccept {
            mapState = it
            isLoading = false
        }

    Column(
        modifier = Modifier
            .padding(top = PaddingL, start = PaddingL, end = PaddingL)
    ) {
        if (isLoading) {
            Text(text = "${Translation.Button.LOADING}...")
        } else {
            when (mapState) {
                null -> {
                    Column {
                        Text(text = Translation.Error.UnexpectedError)

                        Button(onClick = {
                            menuNavController.navigate(MenuRoute.SearchGame.route)
                        }) {
                            Text(text = Translation.Button.BACK_TO_MAIN_MENU)
                        }
                    }
                }

                else -> Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    mapState?.let { ServerMapComponent(it) }
                }
            }
        }
    }
}
