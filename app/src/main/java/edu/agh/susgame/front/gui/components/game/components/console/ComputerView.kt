package edu.agh.susgame.front.gui.components.game.components.console

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.gui.components.common.theme.Header
import edu.agh.susgame.front.gui.components.common.theme.PaddingS
import edu.agh.susgame.front.managers.GameManager

@Composable
fun ComputerComponent(
    gameService: GameService,
    gameManager: GameManager
) {
    var newMessageInputValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.Cyan)

    ) {
        Header(title = Translation.Game.COMPUTER)

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = PaddingS),
                value = newMessageInputValue,
                onValueChange = { newMessageInputValue = it },
                singleLine = true,
            )

            Button(onClick = {
                gameService.sendSimpleMessage(newMessageInputValue)
                gameManager.addMessage(
                    GameService.SimpleMessage(
                        PlayerNickname("You"),
                        newMessageInputValue
                    )
                )
                newMessageInputValue = ""
            }) {
                Text(text = Translation.Button.SEND)
            }
        }

        gameManager.chatMessages.forEach { message ->
            Text(text = message)
        }

    }

    // TODO GAME-54
}