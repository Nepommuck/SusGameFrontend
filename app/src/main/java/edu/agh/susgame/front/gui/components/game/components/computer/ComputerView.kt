package edu.agh.susgame.front.gui.components.game.components.computer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.gui.components.game.components.computer.chat.ChatColors
import edu.agh.susgame.front.gui.components.game.components.computer.chat.ChatComponent
import edu.agh.susgame.front.gui.components.game.components.computer.desktop.DesktopComponent
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.service.interfaces.GameService


@Composable
fun ComputerComponent(
    gameService: GameService,
    gameManager: GameManager
) {
    val computerState = remember { mutableStateOf<ComputerState>(ComputerState.NothingOpened) }

    Row(
        modifier = Modifier
            .background(ChatColors.BACKGROUND)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            DesktopComponent(computerState)
        }
        Box(modifier = Modifier.weight(1f)) {
            when (computerState.value) {
                ComputerState.NothingOpened ->
                    Box {}

                ComputerState.ChatOpened ->
                    ChatComponent(gameService, gameManager)

                is ComputerState.MiniGameOpened -> Box {
                    val miniGame = (computerState.value as ComputerState.MiniGameOpened).miniGame
                    Text("Minigame $miniGame")
                }
            }
        }
    }
}
