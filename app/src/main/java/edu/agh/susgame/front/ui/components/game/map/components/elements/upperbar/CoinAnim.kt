package edu.agh.susgame.front.ui.components.game.map.components.elements.upperbar

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import edu.agh.susgame.R
import edu.agh.susgame.front.ui.components.common.util.Calculate
import kotlinx.coroutines.delay

private const val DELAY_MS: Long = 70
private val ANIMATION_FRAMES = listOf(
    R.drawable.token0,
    R.drawable.token1,
    R.drawable.token2,
    R.drawable.token3,
    R.drawable.token4,
    R.drawable.token5,
    R.drawable.token6,
)

@Composable
fun CoinAnim() {
    var animFrameIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(DELAY_MS)
            animFrameIndex = Calculate.incrementAnimIndex(animFrameIndex, ANIMATION_FRAMES.size)
        }
    }

    Image(
        painter = painterResource(id = ANIMATION_FRAMES[animFrameIndex]),
        contentDescription = null,
    )
}

