package edu.agh.susgame.front.providers.web.socket

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

suspend fun DefaultClientWebSocketSession.serverOutputMessages(messageFlow: MutableSharedFlow<String>) {
    for (message in incoming) {
        println(message)
        val messageStr = (message as? Frame.Text ?: continue).readText()

        messageFlow.emit(messageStr)
    }
}

suspend fun DefaultClientWebSocketSession.inputMessages(messageFlow: Flow<String>) {
    messageFlow.collect { message ->
        send(message)
    }
}

class SocketDemo(
    private val userInputFlow: Flow<String>,
    private val serverOutputFlow: MutableSharedFlow<String>,
) {
    fun init() {
        println("1")
        val webSocketClient = HttpClient {
            install(WebSockets)
        }
        println("2")
        CoroutineScope(Dispatchers.Main).launch {
            println("3")
            webSocketClient.webSocket(
                method = HttpMethod.Get,
//                host = "0.0.0.0",
                // TODO GAME-24
                host = "192.168.140.5",
                port = 8080,
                path = "/games/join?gameId=0&playerName=player1",
            ) {
                println("4")
                val messageOutputRoutine = launch { serverOutputMessages(serverOutputFlow) }
                val userInputRoutine = launch { inputMessages(userInputFlow) }

                userInputRoutine.join() // Wait for completion; either "exit" or error
                messageOutputRoutine.cancelAndJoin()
                println("5")
            }
        }
    }
}
