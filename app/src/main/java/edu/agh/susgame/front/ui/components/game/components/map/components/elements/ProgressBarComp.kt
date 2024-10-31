package edu.agh.susgame.front.ui.components.game.components.map.components.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.agh.susgame.front.ui.components.common.theme.PaddingL
import edu.agh.susgame.front.ui.components.game.components.map.components.elements.progressbar.AnimationComp
import edu.agh.susgame.front.ui.components.game.components.map.components.elements.progressbar.BarComp

private const val width: Float = 0.5f
private const val height: Float = 0.2f

@Composable
fun ProgressBarComp(
    packetsReceived: Int,
    packetsToWin: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(width)
            .fillMaxHeight(height)
            .padding(PaddingL)

    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Cyan)
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            AnimationComp()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(10f)
        ) {
            BarComp(packetsReceived = packetsReceived, packetsToWin = packetsToWin)

        }
    }
}


