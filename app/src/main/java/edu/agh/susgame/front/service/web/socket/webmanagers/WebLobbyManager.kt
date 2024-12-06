package edu.agh.susgame.front.service.web.socket.webmanagers

import androidx.compose.ui.graphics.Color
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.socket.ServerSocketMessage
import edu.agh.susgame.front.gui.components.common.util.player.PlayerStatus
import edu.agh.susgame.front.managers.LobbyManager

class WebLobbyManager(
    private val lobbyManager: LobbyManager
) {
    fun handlePlayerChangingReadinessResponse(decodedMessage: ServerSocketMessage.PlayerChangeReadiness) {
        val newStatus = when(decodedMessage.state){
            true -> PlayerStatus.READY
            false -> PlayerStatus.NOT_READY
        }
        lobbyManager.updatePlayerStatus(
            playerId = PlayerId(decodedMessage.playerId),
            status = newStatus
        )

    }

    fun handlePlayerJoiningResponse(decodedMessage: ServerSocketMessage.PlayerJoining) {
        lobbyManager.updatePlayerJoins(
            nickname = PlayerNickname(decodedMessage.playerName),
            playerId = PlayerId(decodedMessage.playerId),
        )
    }

    fun handlePlayerLeavingResponse(decodedMessage: ServerSocketMessage.PlayerLeaving) {
        lobbyManager.updatePlayerLeaves(
            playerId = PlayerId(decodedMessage.playerId)
        )
    }

    fun handleIdConfig(decodedMessage: ServerSocketMessage.IdConfig) {
        lobbyManager.updateLocalPlayerId(
            playerId = PlayerId(decodedMessage.id)
        )
        lobbyManager.updateFromRest()
    }

    fun handleGameStarted() {
        lobbyManager.loadMapFromServer()
    }

    fun handleColorChange(decodedMessage: ServerSocketMessage.PlayerChangeColor) {
        lobbyManager.updatePlayerColor(
            playerId = PlayerId(decodedMessage.playerId),
            color = Color(decodedMessage.color.decimalRgbaValue.toULong()),
        )
    }
}
