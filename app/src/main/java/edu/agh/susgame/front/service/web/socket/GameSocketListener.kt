package edu.agh.susgame.front.service.web.socket

import edu.agh.susgame.dto.ServerSocketMessage
import edu.agh.susgame.front.model.PlayerNickname
import edu.agh.susgame.front.service.interfaces.GameService.Companion.SimpleMessage
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
            when (
                val decodedMessage = Cbor
                    .decodeFromByteArray<ServerSocketMessage>(bytes.toByteArray())
            ) {
                is ServerSocketMessage.ChatMessage -> {
                    _messagesFlow.emit(
                        SimpleMessage(
                            author = PlayerNickname(decodedMessage.authorNickname),
                            message = decodedMessage.message,
                        )
                    )
                }

                else -> {}
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
