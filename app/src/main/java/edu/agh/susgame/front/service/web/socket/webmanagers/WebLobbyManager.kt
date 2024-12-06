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
        if (decodedMessage.state) {
            lobbyManager.updatePlayerStatus(PlayerId(decodedMessage.playerId), PlayerStatus.READY)
        } else {
            lobbyManager.updatePlayerStatus(
                PlayerId(decodedMessage.playerId),
                PlayerStatus.NOT_READY
            )
        }
    }

    fun handlePlayerJoiningResponse(decodedMessage: ServerSocketMessage.PlayerJoining) {
        lobbyManager.updateAddPlayer(
            nickname = PlayerNickname(decodedMessage.playerName),
            playerId = PlayerId(decodedMessage.playerId),
        )
    }

    fun handlePlayerLeavingResponse(decodedMessage: ServerSocketMessage.PlayerLeaving) {
        lobbyManager.updateRemovePlayer(PlayerId(decodedMessage.playerId))
    }

    fun handleIdConfig(decodedMessage: ServerSocketMessage.IdConfig) {
        lobbyManager.localPlayerId = PlayerId(decodedMessage.id)
        lobbyManager.updateFromRest()
    }

    fun handleGameStarted(decodedMessage: ServerSocketMessage.GameStarted) {
        lobbyManager.loadMapFromServer()
    }

    fun handleColorChange(decodedMessage: ServerSocketMessage.PlayerChangeColor) {
        lobbyManager.updatePlayerColor(
            PlayerId(decodedMessage.playerId),
            Color(decodedMessage.color)
        )
    }
}