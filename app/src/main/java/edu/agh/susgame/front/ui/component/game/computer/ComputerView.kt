package edu.agh.susgame.front.ui.component.game.computer

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.component.common.Header

@Composable
fun ComputerComponent() {
    Column {
        Header(title = Translation.Game.COMPUTER)
    }

    // TODO GAME-54
}
