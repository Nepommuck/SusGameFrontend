package edu.agh.susgame.front.gui.components.game.components.computer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.service.interfaces.GameService

@Composable
fun ComputerComponent(
    gameService: GameService,
    gameManager: GameManager
) {
    Row(
        modifier = Modifier
            .background(Color.Black)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            Text("hello")
        }
        Box(modifier = Modifier.weight(1f)) {
            ChatComponent(gameService, gameManager)
        }
    }
}
