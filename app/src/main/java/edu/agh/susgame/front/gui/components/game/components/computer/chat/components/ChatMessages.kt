package edu.agh.susgame.front.gui.components.game.components.computer.chat.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.gui.components.game.components.computer.chat.ChatColors
import edu.agh.susgame.front.service.interfaces.GameService


@Composable
fun ChatMessages(
    chatMessages: SnapshotStateList<GameService.SimpleMessage>,
    chatScrollState: ScrollState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(chatScrollState)
    ) {
        chatMessages.reversed().forEach { message ->
            Text(
                text = "> [${message.author.value}]: ${message.message}",
                color = ChatColors.TEXT,
            )
        }
    }
}
