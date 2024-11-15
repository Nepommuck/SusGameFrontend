package edu.agh.susgame.front.service.web.socket.webmanagers

import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.rest.model.PlayerREST
import edu.agh.susgame.dto.socket.ServerSocketMessage
import edu.agh.susgame.front.gui.components.common.util.player.PlayerStatus
import edu.agh.susgame.front.managers.LobbyManager

class WebLobbyManager(
    private val lobbyManager: LobbyManager
) {
    fun handlePlayerChangingReadinessResponse(decodedMessage: ServerSocketMessage.PlayerChangeReadinessResponse) {
        if (decodedMessage.state) {
            lobbyManager.updatePlayerStatus(PlayerId(decodedMessage.playerId), PlayerStatus.READY)
        } else {
            lobbyManager.updatePlayerStatus(
                PlayerId(decodedMessage.playerId),
                PlayerStatus.NOT_READY
            )
        }
    }

    fun handlePlayerJoiningResponse(decodedMessage: ServerSocketMessage.PlayerJoiningResponse) {
        lobbyManager.addPlayerRest(
            PlayerREST(
                PlayerNickname(decodedMessage.playerName),
                PlayerId(decodedMessage.playerId),
                12233
            )
        )
    }

    fun handlePlayerLeavingResponse(decodedMessage: ServerSocketMessage.PlayerLeavingResponse) {
        lobbyManager.deletePlayer(PlayerId(decodedMessage.playerId))
    }

    fun handleIdConfig(decodedMessage: ServerSocketMessage.IdConfig) {
        lobbyManager.localId = PlayerId(decodedMessage.id)
    }

    fun test() {
        lobbyManager.addPlayerRest(PlayerREST(PlayerNickname("TEST"), PlayerId(2), 123432))
    }
}