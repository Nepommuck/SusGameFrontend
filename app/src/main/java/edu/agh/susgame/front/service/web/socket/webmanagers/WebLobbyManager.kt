package edu.agh.susgame.front.service.web.socket.webmanagers

import androidx.compose.ui.graphics.Color
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.rest.model.PlayerREST
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
        lobbyManager.addPlayerRest(
            PlayerREST(
                PlayerNickname(decodedMessage.playerName),
                PlayerId(decodedMessage.playerId),
            )
        )
    }

    fun handlePlayerLeavingResponse(decodedMessage: ServerSocketMessage.PlayerLeaving) {
        lobbyManager.removePlayer(PlayerId(decodedMessage.playerId))
    }

    fun handleIdConfig(decodedMessage: ServerSocketMessage.IdConfig) {
        lobbyManager.localPlayer.id = PlayerId(decodedMessage.id)
        lobbyManager.addLocalPlayer()
    }

    fun handleGameStarted(decodedMessage: ServerSocketMessage.GameStarted) {
        lobbyManager.getMapFromServer()
    }

    fun handleColorChange(decodedMessage: ServerSocketMessage.PlayerChangeColor) {
        lobbyManager.setPlayerColor(PlayerId(decodedMessage.playerId), Color(decodedMessage.color))
    }
}
