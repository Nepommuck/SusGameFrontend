package edu.agh.susgame.front.ui.component.game.computer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.providers.interfaces.GameService
import edu.agh.susgame.front.providers.web.socket.WebGameService
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.component.common.Header

@Composable
fun ComputerComponent(webGameService: GameService) {
    val messages = remember { mutableStateListOf<String>() }

    LaunchedEffect(webGameService) {
        webGameService.messagesFlow.collect { message ->
            messages.add(message)
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Header(title = Translation.Game.COMPUTER)

        messages.forEach { message ->
            Text(text = message)
        }
    }

    // TODO GAME-54
}
