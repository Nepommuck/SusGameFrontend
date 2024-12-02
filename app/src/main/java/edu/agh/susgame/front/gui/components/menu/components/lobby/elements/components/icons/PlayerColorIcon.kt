package edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.common.util.player.PlayerLobby
import edu.agh.susgame.front.managers.LobbyManager

@Composable
fun PlayerColorIcon(
    player: PlayerLobby,
    lobbyManager: LobbyManager
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
        Box(modifier = Modifier
            .fillMaxSize(0.9f)
            .background(player.color.value, shape = RoundedCornerShape(20.dp))
            .border(2.dp, Color.Black, shape = RoundedCornerShape(20.dp))
            .let {
                if (player.id == lobbyManager.localPlayer.id) {
                    it.clickable {
                        lobbyManager.isColorBeingChanged.value =
                            !lobbyManager.isColorBeingChanged.value
                    }
                } else it
            }
        )
    }
}