package edu.agh.susgame.front.gui.components.game.components.computer.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.agh.susgame.front.gui.components.common.theme.Header
import edu.agh.susgame.front.gui.components.common.theme.PaddingL
import edu.agh.susgame.front.gui.components.common.theme.PaddingM
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.game.components.computer.chat.components.ChatButtons
import edu.agh.susgame.front.gui.components.game.components.computer.chat.components.ChatMessages
import edu.agh.susgame.front.gui.components.game.components.computer.chat.components.NewChatMessageInput
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.service.interfaces.GameService


object ChatColors {
    val TEXT = Color.Green
    val BACKGROUND = Color.Black
}

@Composable
fun ChatComponent(
    gameService: GameService,
    gameManager: GameManager
) {
    val scrollState = rememberScrollState()
    val newMessageInputValue = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(PaddingL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(PaddingM),
    ) {
        Header(title = Translation.Game.CHAT, color = ChatColors.TEXT)

        NewChatMessageInput(inputValue = newMessageInputValue)

        ChatButtons(
            chatScrollState = scrollState,
            chatCoroutineScope = coroutineScope,
            newMessageInputValue, gameService, gameManager
        )

        ChatMessages(
            chatMessages = gameManager.chatMessages,
            chatScrollState = scrollState,
        )
    }
}
