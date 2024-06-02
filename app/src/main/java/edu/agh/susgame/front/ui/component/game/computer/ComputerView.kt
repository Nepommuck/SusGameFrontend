package edu.agh.susgame.front.ui.component.game.computer

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.providers.web.socket.SocketDemo
import edu.agh.susgame.front.ui.Translation
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun ComputerComponent(lobbyId: LobbyId) {
    val userInputFlow = MutableSharedFlow<String>()
    val serverResponseFlow = MutableSharedFlow<String>()

    val chatHistory = mutableListOf<String>()

    LaunchedEffect(serverResponseFlow) {
//        CoroutineScope(Dispatchers.Main).launch {
        serverResponseFlow.collect { message ->
            chatHistory.add(message)
//            }
        }
    }

    // Unreachable code
    SocketDemo(userInputFlow, serverResponseFlow).init()

    Column {
        Text(text = Translation.Game.COMPUTER)

        Button(onClick = { userInputFlow.tryEmit("123") }) {
            Text(text = "Emit")
        }

        Text(text = "State:")

    }


    // TODO GAME-54
}
