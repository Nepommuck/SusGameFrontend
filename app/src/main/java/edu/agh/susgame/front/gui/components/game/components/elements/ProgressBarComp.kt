package edu.agh.susgame.front.gui.components.game.components.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.gui.components.common.theme.PaddingL
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Calculate
import edu.agh.susgame.front.gui.components.game.components.elements.upperbar.BarComp
import edu.agh.susgame.front.gui.components.game.components.elements.upperbar.CoinAnim
import edu.agh.susgame.front.gui.components.game.components.elements.upperbar.LoadAnim
import edu.agh.susgame.front.managers.GameManager
import java.util.Locale
import kotlin.math.roundToInt

private const val HEIGHT: Float = 0.17f

@Composable
fun ProgressBarComp(
    gameManager: GameManager
) {
    val packetsReceived by remember { gameManager.getServerReceivedPackets() }
    val playerTokens by remember { gameManager.getPlayerTokens(gameManager.localPlayerId) }
    val timeLeft by remember { gameManager.gameTimeLeft }

    Row(
        modifier = Modifier
            .fillMaxHeight(HEIGHT)
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(PaddingL)
                .weight(1f),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                LoadAnim()
            }

            Spacer(modifier = Modifier.weight(0.5f))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(10f),

                ) {
                BarComp(gameManager)
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(13f),
                contentAlignment = Alignment.Center

            ) {
                val percentage: String = (Calculate.getFloatProgress(
                    packetsReceived,
                    gameManager.packetsToWin
                ) * 100).roundToInt().toString()

                Text(
                    text = "$percentage% Critical Network Data", style = TextStyler.TerminalS
                )
            }
        }
        Row(
            Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(PaddingL)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .weight(6f)
                    .fillMaxHeight()
            ) {
                Box(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    CoinAnim()
                }
                Box(
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "$playerTokens ByteTokens",
                        style = TextStyler.TerminalS
                    )

                }
                Box(
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "     ${getTime(timeLeft)}",
                        style = TextStyler.TerminalS,
                    )
                }
            }
        }
    }
}

fun getTime(gameTimeLeft: Int): String {
    val minutes = gameTimeLeft / 60
    val remainingSeconds = gameTimeLeft % 60
    return String.format(Locale.US, "%02d:%02d", minutes, remainingSeconds)
}