package edu.agh.susgame.front.ui.components.menu.components.createlobby.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.Config
import edu.agh.susgame.front.Translation
import edu.agh.susgame.front.ui.components.common.theme.PaddingL
import java.util.Locale

@Composable
fun GameTimeComp(
    gameTime: Int,
    onGameTimeChange: (Int) -> Unit
) {
    Row(
        Modifier
            .padding(PaddingL)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            modifier = Modifier.weight(0.2f),
            text = "${Translation.CreateGame.GAME_TIME}:"
        )
        Text(
            text = String.format(
                locale = Locale.getDefault(),
                format = "%02d",
                gameTime
            ) + " ${Translation.CreateGame.MINUTES} "
        )
        Spacer(
            modifier = Modifier.width(PaddingL)
        )
        Slider(
            modifier = Modifier.weight(0.3f),
            value = gameTime.toFloat(),
            onValueChange = { newValue ->
                onGameTimeChange(newValue.toInt())
            },
            valueRange = Config.gameConfig.gameTimeMinutes.min.toFloat()..Config.gameConfig.gameTimeMinutes.max.toFloat(),
            steps = (Config.gameConfig.gameTimeMinutes.max - Config.gameConfig.gameTimeMinutes.min - 1)
        )
    }
}