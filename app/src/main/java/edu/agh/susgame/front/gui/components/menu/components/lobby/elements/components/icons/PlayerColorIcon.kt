package edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.icons

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.common.util.player.PlayerLobby
import edu.agh.susgame.front.managers.LobbyManager

@Composable
fun PlayerColorIcon(
    player: PlayerLobby,
    lobbyManager: LobbyManager
) {
    var isColorBeingChanged by remember { lobbyManager.lobbyState.isColorBeingChanged }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.player),
            contentDescription = "Player Color Icon",
            modifier = Modifier
                .fillMaxSize(),
            colorFilter = ColorFilter.lighting(
                multiply = player.color.value,
                add = Color.Black
            )
        )
        if (player.id == lobbyManager.localPlayerId) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        isColorBeingChanged = !isColorBeingChanged
                    }
            )
        }
    }
}
