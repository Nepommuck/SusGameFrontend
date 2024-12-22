package edu.agh.susgame.front.gui.components.game.components.elements.rejoin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.game.components.elements.Background
import edu.agh.susgame.front.managers.LobbyManager
import edu.agh.susgame.front.service.interfaces.GameService


@Composable
fun RejoinGameView(
    lobbyManager: LobbyManager,
    gameService: GameService,
    navController: NavController,
) {
    val rejoinButtonCounter = remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        Background()

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                Translation.Game.DISCONNECTED_FROM_GAME_MESSAGE,
                style = TextStyler.TerminalM.copy(color = Color.White),
                textAlign = TextAlign.Center,
            )

            if (rejoinButtonCounter.intValue > 20) {
                PapalRow()
            }

            ButtonsRow(
                lobbyManager,
                gameService,
                navController,
                rejoinButtonCounter,
            )
        }
    }
}
