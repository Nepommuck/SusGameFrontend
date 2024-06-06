package edu.agh.susgame.front.providers.socket

import edu.agh.susgame.front.model.PlayerNickname
import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.providers.web.rest.AbstractRest
import edu.agh.susgame.front.util.AppConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.util.concurrent.CompletableFuture

class GameSocket(webConfig: AppConfig.WebConfig) : AbstractRest(webConfig, "games") {
    private val client = OkHttpClient()
    private val manager = WebSocketManager()
    private val listener = MyWebSocketListener(manager)

    private var socket: WebSocket? = null
    private var currentLobbyId: LobbyId? = null

    val messagesFlow = manager.messagesFlow
    val byteFlow = manager.bytesFlow

    // TODO GAME-59
    fun hasJoinedLobby(lobbyId: LobbyId): Boolean =
        this.currentLobbyId == lobbyId

    fun join(lobbyId: LobbyId, nickname: PlayerNickname): CompletableFuture<Unit> =
        CompletableFuture.supplyAsync {
            val url = baseUrlBuilder()
                .addPathSegment("join")
                .addQueryParameter("gameId", lobbyId.value.toString())
                .addQueryParameter("playerName", nickname.value)
                .build()

            val request = Request.Builder()
                .url(url)
                .build()

            socket = client.newWebSocket(request, listener)

            // Why is it needed? I don't know xd. GPT wrote:
            // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
            client.dispatcher.executorService.shutdown()
        }

    // TODO Game-59 Leave lobby
    fun leave(): CompletableFuture<Unit> =
        CompletableFuture.supplyAsync {
        }
}
