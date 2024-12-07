package edu.agh.susgame.front.gui.components.game.components.elements.upperbar

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import edu.agh.susgame.front.gui.components.common.util.Calculate
import edu.agh.susgame.front.managers.ResourceManager
import kotlinx.coroutines.delay

@Composable
fun CoinAnim() {
    var animFrameIndex by remember { mutableIntStateOf(0) }
    val anim = ResourceManager.TOKEN_ANIMATION

    LaunchedEffect(Unit) {
        while (true) {
            delay(anim.frameRate)
            animFrameIndex = Calculate.incrementAnimIndex(animFrameIndex, anim.frames.size)
        }
    }

    Image(
        painter = painterResource(id = anim.frames[animFrameIndex]),
        contentDescription = null,
    )
}
