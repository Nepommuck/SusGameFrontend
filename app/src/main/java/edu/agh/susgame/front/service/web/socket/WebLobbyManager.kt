package edu.agh.susgame.front.service.web.socket

import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.rest.model.PlayerREST
import edu.agh.susgame.front.ui.components.common.managers.LobbyManager

class WebLobbyManager(
    private val lobbyManager: LobbyManager
) {
    fun test() {
        lobbyManager.addPlayerRest(PlayerREST(PlayerNickname("TEST"), PlayerId(2), 123432))
    }
}