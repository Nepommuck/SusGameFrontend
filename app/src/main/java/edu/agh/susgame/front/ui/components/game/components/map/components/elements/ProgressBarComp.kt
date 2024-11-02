package edu.agh.susgame.front.ui.components.game.components.map.components.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.ui.components.common.theme.PaddingL
import edu.agh.susgame.front.ui.components.common.theme.TextStyler
import edu.agh.susgame.front.ui.components.common.util.Calculate
import edu.agh.susgame.front.ui.components.game.components.map.components.elements.upperbar.BarComp
import edu.agh.susgame.front.ui.components.game.components.map.components.elements.upperbar.CoinAnim
import edu.agh.susgame.front.ui.components.game.components.map.components.elements.upperbar.LoadAnim
import kotlin.math.roundToInt

private const val height: Float = 0.17f
private val tempPlayerMoney: Int = 512

@Composable
fun ProgressBarComp(
    packetsReceived: Int,
    packetsToWin: Int
) {
    Row(
        modifier = Modifier
            .fillMaxHeight(height)
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
                BarComp(packetsReceived = packetsReceived, packetsToWin = packetsToWin)
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(13f),
                contentAlignment = Alignment.Center

            ) {
                val percentage: String = (Calculate.getFloatProgress(
                    packetsReceived,
                    packetsToWin
                ) * 100).roundToInt().toString()

                Text(
                    text = "$percentage% Critical Network Data", style = TextStyler.TerminalMedium
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
            Row(modifier = Modifier.weight(6f).fillMaxHeight()) {
                Box(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    CoinAnim()
                }
                Box(
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("$tempPlayerMoney ByteTokens", style = TextStyler.TerminalMedium)
                }
            }
        }


    }
}




