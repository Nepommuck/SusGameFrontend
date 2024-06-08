package edu.agh.susgame.front.providers.mock

import edu.agh.susgame.front.model.Player
import edu.agh.susgame.front.model.PlayerNickname
import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.providers.interfaces.GameService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okio.ByteString
import java.util.concurrent.CompletableFuture

class MockGameService(private val lobbyService: MockLobbiesProvider) : GameService {
    private val _messagesFlow = MutableSharedFlow<String>()
    private val _byteFlow = MutableSharedFlow<ByteString>()

    private var joinedLobbyInfo: Pair<PlayerNickname, LobbyId>? = null

    override val messagesFlow = _messagesFlow.asSharedFlow()
    override val byteFlow = _byteFlow.asSharedFlow()

    override fun isPlayerInLobby(lobbyId: LobbyId): Boolean =
        when (val lobbyInfo = joinedLobbyInfo) {
            null -> false
            else -> lobbyService.hasPlayerJoinedLobby(lobbyId, playerNickname = lobbyInfo.first)
        }

    override fun joinLobby(lobbyId: LobbyId, nickname: PlayerNickname): CompletableFuture<Unit> =
        lobbyService.joinLobby(lobbyId, Player(nickname))

    override fun leaveLobby(): CompletableFuture<Unit> =
        when (val lobbyInfo = joinedLobbyInfo) {
            null -> CompletableFuture.supplyAsync { }
            else -> lobbyService.leaveLobby(
                lobbyId = lobbyInfo.second,
                playerNickname = lobbyInfo.first,
            )
        }

    override fun sendMessage(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            _messagesFlow.emit(message)
        }
    }
}
