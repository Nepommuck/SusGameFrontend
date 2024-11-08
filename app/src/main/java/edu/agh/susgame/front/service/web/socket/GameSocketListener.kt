package edu.agh.susgame.front.service.web.socket

import androidx.compose.runtime.MutableState
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.socket.ServerSocketMessage
import edu.agh.susgame.front.service.interfaces.GameService.SimpleMessage
import edu.agh.susgame.front.ui.graph.GameManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class GameWebSocketListener : WebSocketListener() {
    private val _socketOpenedFlow = MutableSharedFlow<WebSocket>()
    private val _socketClosedFlow = MutableSharedFlow<Unit>()

    private val _messagesFlow = MutableSharedFlow<SimpleMessage>()

    val socketOpenedFlow: SharedFlow<WebSocket> = _socketOpenedFlow.asSharedFlow()
    val socketClosedFlow: SharedFlow<Unit> = _socketClosedFlow.asSharedFlow()

    val messagesFlow: SharedFlow<SimpleMessage> = _messagesFlow.asSharedFlow()

    var gameManager: MutableState<GameManager>? = null

    fun initGameMapFront(gameMap: MutableState<GameManager>) {
        gameManager = gameMap
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        println("WebSocket opened: ${response.message}")

        CoroutineScope(Dispatchers.Main).launch {
            _socketOpenedFlow.emit(webSocket)
        }
    }


    override fun onMessage(webSocket: WebSocket, text: String) {
        println("WebSocket Receiving text: $text")
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        println("WebSocket Receiving bytes: ${bytes.hex()}")

        CoroutineScope(Dispatchers.Main).launch {

            val decodedMessage: ServerSocketMessage? = try {
                val byteArray = bytes.toByteArray()
                Cbor.decodeFromByteArray<ServerSocketMessage>(byteArray)
            } catch (e: Exception) {
                println("Error processing message: ${e.localizedMessage}")
                null
            }

            decodedMessage?.let {
                when (it) {
                    is ServerSocketMessage.ChatMessage -> {
                        gameManager?.value?.addMessage(
                            SimpleMessage(
                                author = PlayerNickname(it.authorNickname),
                                message = it.message,
                            )
                        )
                    }
                    is ServerSocketMessage.GameState -> {
                        println("GameState message: $it")
                        gameManager?.value?.packetsRec?.value = it.servers[0].packetsReceived
                    }
                    is ServerSocketMessage.ServerError -> {
                        println("Server error: $it")
                    }
                }
            } ?: run {
                println("Failed to decode the message or unsupported message type")
            }

        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        println("WebSocket Closing: $code / $reason")

        CoroutineScope(Dispatchers.Main).launch {
            _socketClosedFlow.emit(Unit)
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        println(
            """
                WebSocket Error: ${t.message} 
                Stack trace: \
                ${t.stackTrace.toList().joinToString("\n") { it.toString() }}")
            """.trimIndent()
        )
    }
}
