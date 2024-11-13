package edu.agh.susgame.front.service.web.socket

import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.rest.model.PlayerREST
import edu.agh.susgame.dto.socket.ServerSocketMessage
import edu.agh.susgame.front.ui.components.common.managers.LobbyManager
import edu.agh.susgame.front.ui.components.common.util.player.PlayerStatus

class WebLobbyManager(
    private val lobbyManager: LobbyManager
) {
    fun handlePlayerChangingReadinessResponse(decodedMessage: ServerSocketMessage.PlayerChangeReadinessResponse) {
        lobbyManager.playersMap[PlayerId(decodedMessage.playerId)]?.status?.value =
            if (decodedMessage.state) PlayerStatus.READY else PlayerStatus.NOT_READY
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

    fun test() {
        lobbyManager.addPlayerRest(PlayerREST(PlayerNickname("TEST"), PlayerId(2), 123432))
    }
}