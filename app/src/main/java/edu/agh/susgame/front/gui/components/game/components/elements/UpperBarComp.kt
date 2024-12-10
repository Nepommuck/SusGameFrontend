package edu.agh.susgame.front.gui.components.game.components.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.common.theme.PaddingXL
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.game.components.elements.upperbar.BarComp
import edu.agh.susgame.front.gui.components.game.components.elements.upperbar.CoinAnim
import edu.agh.susgame.front.gui.components.game.components.elements.upperbar.HourglassImg
import edu.agh.susgame.front.gui.components.game.components.elements.upperbar.MenuButton
import edu.agh.susgame.front.managers.GameManager
import java.util.Locale

@Composable
fun UpperBarComp(
    gameManager: GameManager
) {
    val playerTokens by remember { gameManager.getPlayerTokens(gameManager.localPlayerId) }
    val timeLeft by remember { gameManager.gameTimeLeft }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingXL),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(4f)) {
                BarComp(gameManager)
            }
            Box(modifier = Modifier.weight(3f), contentAlignment = Alignment.Center) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.Center
                ) {
                    CoinAnim()
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        "$playerTokens",
                        style = TextStyler.TerminalM
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    HourglassImg()
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = getTime(timeLeft),
                        style = TextStyler.TerminalM,
                    )
                }
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                MenuButton(
                    onClick = { gameManager.gameState.isMenuOpened.value = !gameManager.gameState.isMenuOpened.value  }
                )
            }
        }
    }
}

fun getTime(gameTimeLeft: Int): String {
    val minutes = gameTimeLeft / 60
    val remainingSeconds = gameTimeLeft % 60
    return String.format(Locale.US, "%02d:%02d", minutes, remainingSeconds)
}