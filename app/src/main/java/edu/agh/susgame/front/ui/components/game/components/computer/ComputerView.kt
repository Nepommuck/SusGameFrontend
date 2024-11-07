package edu.agh.susgame.front.ui.components.game.components.computer

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
import edu.agh.susgame.front.Translation
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.ui.components.common.Header
import edu.agh.susgame.front.ui.components.common.theme.PaddingS
import edu.agh.susgame.front.ui.graph.GameMapFront

@Composable
fun ComputerComponent(
    gameService: GameService,
    gameMapFront: GameMapFront
) {
//    val messages = remember { mutableStateListOf<String>() }
    var newMessageInputValue by remember { mutableStateOf("") }

//    LaunchedEffect(gameService) {
//        gameService.messagesFlow.collect { message ->
//            messages.add("[${message.author.value}]: ${message.message}")
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.Blue)

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
                gameMapFront.addMessage(
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

        gameMapFront.chatMessages.forEach { message ->
            Text(text = message)
        }

    }

    // TODO GAME-54
}
