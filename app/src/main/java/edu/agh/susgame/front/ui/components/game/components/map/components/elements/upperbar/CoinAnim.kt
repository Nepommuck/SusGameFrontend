package edu.agh.susgame.front.ui.components.game.components.map.components.elements.upperbar

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

private const val delayMs: Long = 70
private val animationFrames = listOf(
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
            delay(delayMs)
            animFrameIndex = Calculate.incrementAnimIndex(animFrameIndex, animationFrames.size)
        }
    }

    Image(
        painter = painterResource(id = animationFrames[animFrameIndex]),
        contentDescription = null,
//        modifier = Modifier.fillMaxSize()
    )
}

