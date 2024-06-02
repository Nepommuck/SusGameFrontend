package edu.agh.susgame.front.ui.component.game.computer

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.ui.Translation

@Composable
fun ComputerComponent(lobbyId: LobbyId) {
    Text(text = Translation.Game.COMPUTER)

    // TODO GAME-54
}
