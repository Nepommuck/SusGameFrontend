package edu.agh.susgame.front.service.interfaces

import edu.agh.susgame.front.model.PlayerNickname
import edu.agh.susgame.front.model.game.LobbyId
import kotlinx.coroutines.flow.SharedFlow
import java.util.concurrent.CompletableFuture


interface GameService {
    val messagesFlow: SharedFlow<SimpleMessage>

    fun isPlayerInLobby(lobbyId: LobbyId): Boolean

    fun joinLobby(lobbyId: LobbyId, nickname: PlayerNickname): CompletableFuture<Unit>

    fun leaveLobby(): CompletableFuture<Unit>

    // TODO GAME-64 Should be replaced by less general methods like
    //  `sendRouterUpdate(routerId, newRouterParams), `notifyAboutCreditChange(int)` etc.
    //  when backend is ready
    fun sendSimpleMessage(message: String)

    companion object {
        data class SimpleMessage(val author: PlayerNickname, val message: String)
    }
}
