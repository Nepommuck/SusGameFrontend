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
import edu.agh.susgame.front.gui.components.common.util.Calculate
import edu.agh.susgame.front.gui.components.game.components.elements.upperbar.BarComp
import edu.agh.susgame.front.gui.components.game.components.elements.upperbar.CoinAnim
import edu.agh.susgame.front.gui.components.game.components.elements.upperbar.HourglassImg
import edu.agh.susgame.front.gui.components.game.components.elements.upperbar.LoadAnim
import edu.agh.susgame.front.managers.GameManager
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun UpperBarComp(
    gameManager: GameManager
) {
    val packetsReceived by remember { gameManager.getServerReceivedPackets() }
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
            Box(modifier = Modifier.weight(1f)) {
                LoadAnim()
            }
            Box(modifier = Modifier.weight(5f)) {
                BarComp(gameManager)
            }
            Box(modifier = Modifier.weight(6f), contentAlignment = Alignment.Center) {
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
        }
    }
}

fun packetsRatio(packetsReceived: Int, packetsToWin: Int): String =
    (Calculate.getFloatProgress(packetsReceived, packetsToWin) * 100)
        .roundToInt()
        .toString()


fun getTime(gameTimeLeft: Int): String {
    val minutes = gameTimeLeft / 60
    val remainingSeconds = gameTimeLeft % 60
    return String.format(Locale.US, "%02d:%02d", minutes, remainingSeconds)
}