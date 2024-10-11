package edu.agh.susgame.front.service.interfaces

import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import kotlinx.coroutines.flow.SharedFlow
import java.util.concurrent.CompletableFuture

interface GameService {
    data class SimpleMessage(val author: PlayerNickname, val message: String)

    val messagesFlow: SharedFlow<SimpleMessage>

    fun isPlayerInLobby(lobbyId: LobbyId): Boolean

    fun joinLobby(lobbyId: LobbyId, nickname: PlayerNickname): CompletableFuture<Unit>

    fun leaveLobby(): CompletableFuture<Unit>

    // TODO Should be replaced by less general methods like
    //  `sendRouterUpdate(routerId, newRouterParams), `notifyAboutCreditChange(int)` etc.
    //  when backend is ready
    fun sendSimpleMessage(message: String)
}
