package edu.agh.susgame.front.ui.components.menu.components.lobby.elements.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.front.managers.LobbyManager
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.ui.components.common.theme.PaddingS
import edu.agh.susgame.front.ui.components.common.util.player.PlayerLobby
import edu.agh.susgame.front.ui.components.common.util.player.PlayerStatus
import edu.agh.susgame.front.ui.components.menu.components.lobby.elements.components.icons.PlayerStatusIcon

@Composable
fun PlayerRow(
    id: PlayerId,
    player: PlayerLobby,
    lobbyManager: LobbyManager,
    gameService: GameService,
) {
    Row(
        modifier = Modifier
            .padding(PaddingS)
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(4f)
                .background(Color.Gray)
                .fillMaxSize()
                .align(Alignment.CenterVertically)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterStart),
                text = player.name.value
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.DarkGray)
                .fillMaxSize()
                .align(Alignment.CenterVertically)
                .let {
                    if (id == lobbyManager.localId) {
                        it.clickable { handlePlayerStatusChange(id, lobbyManager, gameService) }
                    } else it
                }
        ) {
            PlayerStatusIcon(playerStatus = player.status)
        }
    }
}

private fun handlePlayerStatusChange(
    id: PlayerId,
    lobbyManager: LobbyManager,
    gameService: GameService
) {
    lobbyManager.localId?.let { localId ->
        if (id == localId) {
            val currentStatus = lobbyManager.getPlayerStatus(localId)
            val newStatus = if (currentStatus == PlayerStatus.READY) {
                PlayerStatus.NOT_READY
            } else {
                PlayerStatus.READY
            }
            gameService.sendChangingStateRequest(localId, newStatus)
            lobbyManager.updatePlayerStatus(localId, newStatus)
        }
    }
}