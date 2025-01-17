package edu.agh.susgame.front.service.web.webservices

import androidx.compose.ui.graphics.Color
import edu.agh.susgame.dto.common.ColorDTO
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.LobbyPin
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.socket.ClientSocketMessage
import edu.agh.susgame.dto.socket.common.GameStatus
import edu.agh.susgame.front.config.utils.Configuration
import edu.agh.susgame.front.gui.components.common.graph.node.NodeId
import edu.agh.susgame.front.gui.components.common.util.player.PlayerStatus
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizQuestion.QuizAnswerId
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizQuestion.QuizQuestionId
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.managers.LobbyManager
import edu.agh.susgame.front.managers.state.GameStateManager
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
    private var gameState: GameStateManager? = null
    private var currentLobbyId: LobbyId? = null
    private var currentLobbyPin: LobbyPin? = null
    private var playerNickname: PlayerNickname? = null

    override val messagesFlow = listener.messagesFlow

    init {
        CoroutineScope(Dispatchers.Main).launch {
            listener.socketOpenedFlow.collect { socket ->
                gameState?.isPlayerDisconnected?.value = false
                this@WebGameService.socket = socket
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            listener.socketClosedFlow.collect {
                socket = null
            }
        }
    }

    override fun initGameManager(gameManager: GameManager) {
        listener.initWebGameManager(gameManager)
        gameState = gameManager.gameState
    }

    override fun initLobbyManager(lobbyManager: LobbyManager) {
        listener.initWebLobbyManager(lobbyManager)
    }


    override fun isPlayerInLobby(lobbyId: LobbyId): Boolean =
        this.currentLobbyId == lobbyId && this.socket != null

    override fun joinLobby(
        lobbyId: LobbyId,
        lobbyPin: LobbyPin?,
        nickname: PlayerNickname,
    ): CompletableFuture<Unit> =
        connectToWebSocket(lobbyId, lobbyPin, nickname)

    override fun rejoinLobby(playerId: PlayerId): CompletableFuture<Unit> {
        val currentLobbyIdSnapshot = currentLobbyId
        val playerNicknameSnapshot = playerNickname

        return if (currentLobbyIdSnapshot == null || playerNicknameSnapshot == null)
            throw IllegalStateException("No lobby was recently joined")
        else connectToWebSocket(
            lobbyId = currentLobbyIdSnapshot,
            lobbyPin = currentLobbyPin,
            nickname = playerNicknameSnapshot,
            playerId = playerId,
        )
    }

    override fun leaveLobby(): CompletableFuture<Unit> =
        CompletableFuture.supplyAsync {
            socket?.close(code = 1000, reason = null)
        }

    override fun sendSimpleMessage(message: String) {
        sendClientSocketMessage(
            ClientSocketMessage.ChatMessage(
                message = message
            )
        )
    }

    override fun sendLeavingRequest(playerId: PlayerId) {
        sendClientSocketMessage(
            ClientSocketMessage.PlayerLeaving(
                playerId = playerId.value
            )
        )

    }

    override fun sendChangePlayerReadinessRequest(playerId: PlayerId, status: PlayerStatus) {
        sendClientSocketMessage(
            ClientSocketMessage.PlayerChangeReadiness(
                playerId = playerId.value,
                state = parseStatus(status)
            )
        )
    }

    override fun sendStartGame() {
        sendClientSocketMessage(
            ClientSocketMessage.GameState(
                gameStatus = GameStatus.RUNNING
            )
        )
    }

    override fun sendPlayerChangeColor(playerId: PlayerId, color: Color) {
        sendClientSocketMessage(
            ClientSocketMessage.PlayerChangeColor(
                playerId = playerId.value,
                color = ColorDTO(color.value.toString())
            )
        )
    }

    override fun sendHostRouteUpdate(hostId: NodeId, packetPath: List<NodeId>) {
        sendClientSocketMessage(
            ClientSocketMessage.HostRouteDTO(
                id = hostId.value,
                packetPath = packetPath.map { it.value },
            )
        )
    }

    override fun sendHostFlow(hostId: NodeId, packets: Int) {
        sendClientSocketMessage(
            ClientSocketMessage.HostFlowDTO(
                id = hostId.value,
                packetsSentPerTick = packets
            )
        )
    }

    override fun sendUpgradeRouter(routerId: NodeId) {
        sendClientSocketMessage(
            ClientSocketMessage.UpgradeDTO(
                deviceId = routerId.value
            )
        )
    }

    override fun sendFixRouter(routerId: NodeId) {
        sendClientSocketMessage(
            ClientSocketMessage.FixRouterDTO(
                deviceId = routerId.value
            )
        )
    }

    override fun sendQuizQuestionAnswer(questionId: QuizQuestionId, answerId: QuizAnswerId) {
        sendClientSocketMessage(
            ClientSocketMessage.QuizAnswerDTO(
                questionId = questionId.value,
                answer = answerId.value,
            )
        )
    }

    private fun connectToWebSocket(
        lobbyId: LobbyId,
        lobbyPin: LobbyPin?,
        nickname: PlayerNickname,
        playerId: PlayerId? = null,
    ): CompletableFuture<Unit> = CompletableFuture.supplyAsync {
        val url = baseUrlBuilder()
            .addPathSegment("join")
            .addQueryParameter("gameId", lobbyId.value.toString())
            .addQueryParameter("playerName", nickname.value)
            .apply {
                if (lobbyPin != null) {
                    addQueryParameter("gamePin", lobbyPin.value)
                }
                if (playerId != null) {
                    addQueryParameter("playerId", playerId.value.toString())
                }
            }.build()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newWebSocket(request, listener)
        currentLobbyId = lobbyId
        currentLobbyPin = lobbyPin
        playerNickname = nickname
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun sendClientSocketMessage(clientSocketMessage: ClientSocketMessage) {
        socket?.send(
            Cbor.encodeToByteArray(clientSocketMessage).toByteString()
        )
    }

    private fun parseStatus(status: PlayerStatus): Boolean =
        when (status) {
            PlayerStatus.READY -> true
            PlayerStatus.NOT_READY -> false
        }
}
