package edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components

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
import edu.agh.susgame.front.gui.components.common.theme.PaddingS
import edu.agh.susgame.front.gui.components.common.util.player.PlayerLobby
import edu.agh.susgame.front.gui.components.common.util.player.PlayerStatus
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.icons.PlayerColorIcon
import edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.icons.PlayerStatusIcon
import edu.agh.susgame.front.managers.LobbyManager
import edu.agh.susgame.front.service.interfaces.GameService

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
            .background(Color.Gray)
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
                    .let {
                        if (id == lobbyManager.localPlayer.id) {
                            println("clicked!")
                            it.clickable { lobbyManager.isColorBeingChanged.value = true }
                        } else it
                    }
            ) {
                PlayerColorIcon(
                    playerColor = lobbyManager.localPlayer.color,
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.DarkGray)
                    .fillMaxSize()
                    .align(Alignment.CenterVertically)
                    .let {
                        if (id == lobbyManager.localPlayer.id) {
                            it.clickable { handlePlayerStatusChange(id, lobbyManager, gameService) }
                        } else it
                    }
            ) {
                PlayerStatusIcon(playerStatus = player.status)
            }

        }
    }
}

private fun handlePlayerStatusChange(
    id: PlayerId,
    lobbyManager: LobbyManager,
    gameService: GameService
) {
    val localId = lobbyManager.localPlayer.id
    if (id == localId) {
        val currentStatus = lobbyManager.getPlayerStatus(localId)
        val newStatus =
            if (currentStatus == PlayerStatus.READY) PlayerStatus.NOT_READY else PlayerStatus.READY
        gameService.sendChangePlayerReadinessRequest(localId, newStatus)
        lobbyManager.updatePlayerStatus(localId, newStatus)
    }
}
