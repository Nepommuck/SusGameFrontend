package edu.agh.susgame.front.gui.components.game.components.computer.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.front.gui.components.common.theme.Header
import edu.agh.susgame.front.gui.components.common.theme.PaddingL
import edu.agh.susgame.front.gui.components.common.theme.PaddingM
import edu.agh.susgame.front.gui.components.common.theme.PaddingS
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.service.interfaces.GameService

@Composable
fun ChatComponent(
    gameService: GameService,
    gameManager: GameManager
) {
    val scrollState = rememberScrollState()
    var newMessageInputValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(PaddingL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(PaddingM),
    ) {
        Header(title = Translation.Game.CHAT, color = Color.Green)

        OutlinedTextField(
            modifier = Modifier
                .padding(end = PaddingS)
                .fillMaxWidth(),
            value = newMessageInputValue,
            onValueChange = { newMessageInputValue = it },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors().copy(
                focusedTextColor = Color.Green,
                unfocusedTextColor = Color.Green,
                cursorColor = Color.Green,
                focusedIndicatorColor = Color.Green,
                unfocusedIndicatorColor = Color.Green,
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Button(
                onClick = {
                    gameService.sendSimpleMessage(newMessageInputValue)
                    gameManager.addMessage(
                        GameService.SimpleMessage(
                            PlayerNickname("You"),
                            newMessageInputValue
                        )
                    )
                    newMessageInputValue = ""
                },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = Color.Black,
                    contentColor = Color.Green,
                ),
                border = BorderStroke(2.dp, Color.Green),
            ) {
                Text(text = Translation.Button.SEND)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            gameManager.chatMessages.forEach { message ->
                Text("> $message", color = Color.Green)
            }
        }
    }
}
