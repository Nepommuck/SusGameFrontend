package edu.agh.susgame.front.gui.components.game.components.computer.chat.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.service.interfaces.GameService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun ChatButtons(
    chatScrollState: ScrollState,
    chatCoroutineScope: CoroutineScope,
    newMessageInputValue: MutableState<String>,
    gameService: GameService,
    gameManager: GameManager
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box {
            if (chatScrollState.value > 50) {
                ChatButton(
                    text = "^",
                    onClick = {
                        chatCoroutineScope.launch {
                            chatScrollState.animateScrollTo(0)
                        }
                    }
                )
            }
        }

        ChatButton(
            text = Translation.Button.SEND,
            onClick = {
                gameManager.handleSendingMessage(text = newMessageInputValue.value)
                newMessageInputValue.value = ""
            },
        )
    }
}
