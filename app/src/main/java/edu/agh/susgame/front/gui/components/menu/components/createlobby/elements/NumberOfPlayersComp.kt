package edu.agh.susgame.front.gui.components.menu.components.createlobby.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import edu.agh.susgame.front.config.AppConfig
import edu.agh.susgame.front.gui.components.common.theme.PaddingL
import edu.agh.susgame.front.gui.components.common.util.Translation

@Composable
fun NumberOfPlayersComp(
    numberOfPlayers: Int,
    onNumberOfPlayersChange: (Int) -> Unit
) {
    Row(
        Modifier
            .padding(PaddingL)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "${Translation.CreateGame.AMOUNT_OF_PLAYERS}:"
        )
        Spacer(modifier = Modifier.width(PaddingL))

        Button(
            onClick = { onNumberOfPlayersChange(numberOfPlayers - 1) },
            enabled = numberOfPlayers > AppConfig.gameConfig.playersPerGame.min
        ) {
            Text(text = "-")
        }

        Text(
            text = "$numberOfPlayers",
            modifier = Modifier.padding(PaddingL),
            fontWeight = FontWeight.Bold
        )

        Button(
            onClick = { onNumberOfPlayersChange(numberOfPlayers + 1) },
            enabled = numberOfPlayers < AppConfig.gameConfig.playersPerGame.max
        ) {
            Text(text = "+")
        }
    }
}