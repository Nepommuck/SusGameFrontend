package edu.agh.susgame.front.ui.components.game.computer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.components.common.Header
import edu.agh.susgame.front.ui.theme.PaddingS

@Composable
fun ComputerComponent(gameService: GameService) {
    val messages = remember { mutableStateListOf<String>() }
    var newMessageInputValue by remember { mutableStateOf("") }

    LaunchedEffect(gameService) {
        gameService.messagesFlow.collect { message ->
            messages.add("[${message.author.value}]: ${message.message}")
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Header(title = Translation.Game.COMPUTER)

        Row(
            modifier = Modifier.fillMaxWidth(),
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
                messages.add("[You]: ${newMessageInputValue}")
                newMessageInputValue = ""
            }) {
                Text(text = Translation.Button.SEND)
            }
        }

        messages.forEach { message ->
            Text(text = message)
        }
    }

    // TODO GAME-54
}
