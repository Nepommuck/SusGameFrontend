package edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.common.theme.PaddingS
import edu.agh.susgame.front.gui.components.common.util.player.PlayerLobby
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.icons.PlayerColorIcon
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.icons.PlayerStatusIcon
import edu.agh.susgame.front.managers.LobbyManager
import edu.agh.susgame.front.service.interfaces.GameService

@Composable
fun PlayerRow(
    player: PlayerLobby,
    lobbyManager: LobbyManager,
    gameService: GameService,
) {

    Row(
        modifier = Modifier
            .padding(PaddingS)
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.Gray, shape = RoundedCornerShape(20.dp))
    ) {
        Box(
            modifier = Modifier
                .weight(2f)

                .fillMaxSize()
                .align(Alignment.CenterVertically)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterStart),
                text = player.name.value
            )
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )
        {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                PlayerColorIcon(
                    player = player,
                    lobbyManager = lobbyManager
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.DarkGray)
                    .fillMaxSize()
                    .align(Alignment.CenterVertically)
                    .let {
                        if (player.id == lobbyManager.localPlayerId) {
                            it.clickable {
                                lobbyManager.handleLocalPlayerStatusChange()
                            }
                        } else it
                    }
            ) {
                PlayerStatusIcon(playerStatus = player.status)
            }
        }
    }
}