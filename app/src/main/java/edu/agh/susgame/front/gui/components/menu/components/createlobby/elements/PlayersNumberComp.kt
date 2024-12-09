package edu.agh.susgame.front.gui.components.menu.components.createlobby.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.agh.susgame.front.config.AppConfig
import edu.agh.susgame.front.gui.components.common.theme.PaddingL
import edu.agh.susgame.front.gui.components.common.theme.PaddingXL
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation

@Composable
fun PlayersNumberComp(
    numOfPlayers: Int,
    onGameTimeChange: (Int) -> Unit
) {
    val playersPerGame = AppConfig.gameConfig.playersPerGame
    Row(
        Modifier
            .padding(PaddingXL)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(3f),
            text = "${Translation.CreateGame.AMOUNT_OF_PLAYERS}: $numOfPlayers",
            style = TextStyler.TerminalM
        )
        Spacer(
            modifier = Modifier.width(PaddingL)
        )
        Slider(
            modifier = Modifier.weight(2f),
            value = numOfPlayers.toFloat(),
            onValueChange = { newValue ->
                onGameTimeChange(newValue.toInt())
            },
            valueRange = playersPerGame.min.toFloat()..playersPerGame.max.toFloat(),
            steps = (playersPerGame.max - playersPerGame.min - 1),
            colors = SliderDefaults.colors(
                thumbColor = Color.White.copy(alpha = 0.9f),
                activeTrackColor = Color.DarkGray,
                inactiveTrackColor = Color.Gray,
            )
        )
    }
}
