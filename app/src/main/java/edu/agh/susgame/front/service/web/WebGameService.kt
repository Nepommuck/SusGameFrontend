package edu.agh.susgame.front.service.web

import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.socket.ClientSocketMessage
import edu.agh.susgame.dto.socket.common.GameStatus
import edu.agh.susgame.front.rest.AbstractRest
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.web.socket.GameWebSocketListener
import edu.agh.susgame.front.ui.graph.GameMapFront
import edu.agh.susgame.front.ui.graph.node.NodeId
import edu.agh.susgame.front.utils.AppConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.encodeToByteArray
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okio.ByteString.Companion.toByteString
import java.util.concurrent.CompletableFuture

class WebGameService(
    webConfig: AppConfig.WebConfig,
) : GameService,
    AbstractRest(webConfig, "games") {

    private val client = OkHttpClient()
    private val listener = GameWebSocketListener()

    private var socket: WebSocket? = null
    private var currentLobbyId: LobbyId? = null
    private var playerNickname: PlayerNickname? = null

    private var gameFront : GameMapFront? = null

    override val messagesFlow = listener.messagesFlow

    init {
        CoroutineScope(Dispatchers.Main).launch {
            listener.socketOpenedFlow.collect { socket ->
                this@WebGameService.socket = socket
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            listener.socketClosedFlow.collect {
                socket = null
                currentLobbyId = null
                playerNickname = null
            }
        }
    }

    override fun initGameFront(gameMapFront: GameMapFront){
        gameFront = gameMapFront
        listener.initGameMapFront(gameMapFront)
    }

    override fun isPlayerInLobby(lobbyId: LobbyId): Boolean =
        this.currentLobbyId == lobbyId && this.socket != null

    override fun joinLobby(lobbyId: LobbyId, nickname: PlayerNickname): CompletableFuture<Unit> =
        CompletableFuture.supplyAsync {
            val url = baseUrlBuilder()
                .addPathSegment("join")
                .addQueryParameter("gameId", lobbyId.value.toString())
                .addQueryParameter("playerName", nickname.value)
                .build()

            val request = Request.Builder()
                .url(url)
                .build()

            client.newWebSocket(request, listener)
            currentLobbyId = lobbyId
            playerNickname = nickname
        }

    override fun leaveLobby(): CompletableFuture<Unit> =
        CompletableFuture.supplyAsync {
            socket?.close(code = 1000, reason = null)
        }

    override fun sendSimpleMessage(message: String) {
        sendClientSocketMessage(
            clientSocketMessage = ClientSocketMessage.ChatMessage(message)
        )
    }

    override fun sendStartGame() {
        sendClientSocketMessage(
            clientSocketMessage = ClientSocketMessage.GameState(gameStatus = GameStatus.RUNNING)
        )
    }

    override fun sendHostUpdate(
        hostId: NodeId,
        packetPath: List<NodeId>,
        packetsSentPerTick: Int,
    ) {
        sendClientSocketMessage(
            clientSocketMessage = ClientSocketMessage.HostDTO(
                id = hostId.value,
                packetPath = packetPath.map { it.value },
                packetsSentPerTick = packetsSentPerTick,
            )
        )

    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun sendClientSocketMessage(clientSocketMessage: ClientSocketMessage) {
        socket?.send(
            Cbor.encodeToByteArray(clientSocketMessage).toByteString()
        )
    }
}
