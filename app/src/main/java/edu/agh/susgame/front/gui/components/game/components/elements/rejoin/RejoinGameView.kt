package edu.agh.susgame.front.gui.components.game.components.elements.rejoin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.game.components.elements.Background
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute
import edu.agh.susgame.front.managers.LobbyManager
import edu.agh.susgame.front.service.interfaces.GameService


@Composable
fun RejoinGameView(
    lobbyManager: LobbyManager,
    gameService: GameService,
    menuNavController: NavController,
) {
    var papalCounter by remember { mutableIntStateOf(0) }

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

            if (papalCounter > 2) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 100.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    Image(
                        painter = painterResource(R.drawable.jp1),
                        contentDescription = "Jan Paweł I",
                        contentScale = ContentScale.Fit,
                    )
                    Image(
                        painter = painterResource(R.drawable.jp2),
                        contentDescription = "Jan Paweł II",
                        contentScale = ContentScale.Fit,
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = {
                        menuNavController.navigate(MenuRoute.MainMenu.route)
                    },
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = Color.White,
                    )
                ) {
                    Text(
                        Translation.Button.LEAVE,
                        style = TextStyler.TerminalS.copy(color = Color.Black)
                    )
                }

                Button(
                    enabled = lobbyManager.localPlayerId != null,
                    onClick = {
                        lobbyManager.localPlayerId?.let { playerId ->
                            gameService.rejoinLobby(playerId)
                        }
                        papalCounter++
                    },
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = Color.Green,
                    ),
                ) {
                    Text(
                        Translation.Button.TRY_RECONNECTING,
                        style = TextStyler.TerminalS.copy(color = Color.White)
                    )
                }
            }
        }
    }
}
