package edu.agh.susgame.front.providers.web.socket

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class GameWebSocketListener(private val webSocketManager: WebSocketManager) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        println("WebSocket opened: ${response.message}")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        println("WebSocket Receiving: $text")
        webSocketManager.onMessageReceived(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        println("WebSocket Receiving bytes: ${bytes.hex()}")

        webSocketManager.onMessageReceived(bytes.hex())
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        println("WebSocket Closing: $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        println("WebSocket Error: ${t.message}")
    }
}
