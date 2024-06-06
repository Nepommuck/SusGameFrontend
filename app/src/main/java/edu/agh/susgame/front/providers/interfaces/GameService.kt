package edu.agh.susgame.front.providers.interfaces

import edu.agh.susgame.front.model.PlayerNickname
import edu.agh.susgame.front.model.game.LobbyId
import kotlinx.coroutines.flow.SharedFlow
import okhttp3.Request
import okio.ByteString
import java.util.concurrent.CompletableFuture

interface GameService {
    // TODO GAME-59 Should be replaced by typed flows like `GameStateFlow` etc.
    //  when backend is ready
    val messagesFlow: SharedFlow<String>
    val byteFlow: SharedFlow<ByteString>

    fun hasJoinedLobby(lobbyId: LobbyId): Boolean

    fun joinLobby(lobbyId: LobbyId, nickname: PlayerNickname): CompletableFuture<Unit>

    fun leaveLobby(): CompletableFuture<Unit>

    // TODO GAME-59 Should be replaced by less general methods like
    //  `sendRouterUpdate(routerId, newRouterParams), `notifyAboutCreditChange(int)` etc.
    //  when backend is ready
    fun sendMessage(message: String): Unit
}