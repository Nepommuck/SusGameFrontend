package edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.player.PlayerLobby
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.icons.PlayerColorIcon
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.icons.PlayerStatusIcon
import edu.agh.susgame.front.managers.LobbyManager

@Composable
fun PlayerRow(
    player: PlayerLobby,
    lobbyManager: LobbyManager,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.frame),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    .weight(3f)
                    .fillMaxSize()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = player.name.value,
                    style = TextStyler.TerminalL
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(0.8f)
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