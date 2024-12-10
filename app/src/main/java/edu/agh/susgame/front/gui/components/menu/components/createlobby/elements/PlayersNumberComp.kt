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
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.config.AppConfig
import edu.agh.susgame.front.gui.components.common.theme.PaddingL
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
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "${Translation.CreateGame.AMOUNT_OF_PLAYERS}: ",
            style = TextStyler.TerminalL
        )
        Text(
            text = "$numOfPlayers",
            style = TextStyler.TerminalInput
        )
        Spacer(
            modifier = Modifier.width(PaddingL)
        )
        Slider(
            value = numOfPlayers.toFloat(),
            onValueChange = { newValue ->
                onGameTimeChange(newValue.toInt())
            },
            valueRange = playersPerGame.min.toFloat()..playersPerGame.max.toFloat(),
            steps = (playersPerGame.max - playersPerGame.min - 1),
            colors = SliderDefaults.colors(
                thumbColor = Color.White.copy(alpha = 0.7f),
                activeTrackColor = Color.DarkGray,
                inactiveTrackColor = Color.Gray,
            )
        )
    }
}
