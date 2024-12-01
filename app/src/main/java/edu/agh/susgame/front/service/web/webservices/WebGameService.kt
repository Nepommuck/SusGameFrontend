package edu.agh.susgame.front.service.web.webservices

import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.socket.ClientSocketMessage
import edu.agh.susgame.dto.socket.common.GameStatus
import edu.agh.susgame.front.config.utils.Configuration
import edu.agh.susgame.front.gui.components.common.graph.node.NodeId
import edu.agh.susgame.front.gui.components.common.util.player.PlayerStatus
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.managers.LobbyManager
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.web.IpAddressProvider
import edu.agh.susgame.front.service.web.rest.AbstractRest
import edu.agh.susgame.front.service.web.socket.GameWebSocketListener
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
    override val webConfig: Configuration.WebConfig,
    override val ipAddressProvider: IpAddressProvider
) : GameService, AbstractRest(endpointName = "games") {

    private val client = OkHttpClient()
    private val listener = GameWebSocketListener()

    private var socket: WebSocket? = null
    private var currentLobbyId: LobbyId? = null
    private var playerNickname: PlayerNickname? = null

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

    override fun initGameManager(gameManager: GameManager) {
        listener.initWebGameManager(gameManager)
    }

    override fun initLobbyManager(lobbyManager: LobbyManager) {
        listener.initWebLobbyManager(lobbyManager)
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

    override fun sendLeavingRequest(playerId: PlayerId) {
        sendClientSocketMessage(
            clientSocketMessage = ClientSocketMessage.PlayerLeaving(playerId.value)
        )
    }

    override fun sendChangePlayerReadinessRequest(playerId: PlayerId, status: PlayerStatus) {
        val stateValue: Boolean = when (status) {
            PlayerStatus.READY -> true
            PlayerStatus.NOT_READY -> false
            PlayerStatus.CONNECTING -> false
        }
        sendClientSocketMessage(
            clientSocketMessage = ClientSocketMessage.PlayerChangeReadiness(
                playerId.value,
                stateValue
            )
        )
    }

    override fun sendStartGame() {
        sendClientSocketMessage(
            clientSocketMessage = ClientSocketMessage.GameState(gameStatus = GameStatus.RUNNING)
        )
    }

    override fun sendPlayerChangeColor(playerId: PlayerId, color: ULong) {
        sendClientSocketMessage(
            clientSocketMessage = ClientSocketMessage.PlayerChangeColor(
                playerId = playerId.value,
                color = color
            )
        )
    }

    override fun sendHostRouteUpdate(
        hostId: NodeId,
        packetPath: List<NodeId>,
    ) {
        sendClientSocketMessage(
            clientSocketMessage = ClientSocketMessage.HostRouteDTO(
                id = hostId.value,
                packetPath = packetPath.map { it.value },
            )
        )
    }

    override fun sendHostFlow(hostId: NodeId, packets: Int) {
        sendClientSocketMessage(
            clientSocketMessage = ClientSocketMessage.HostFlowDTO(
                id = hostId.value,
                packetsSentPerTick = packets
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
