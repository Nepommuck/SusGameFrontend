package edu.agh.susgame.front.gui.components.game.components.elements.upperbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import edu.agh.susgame.front.gui.components.common.util.AssetsManager
import edu.agh.susgame.front.gui.components.common.util.Calculate
import kotlinx.coroutines.delay

@Composable
fun CoinAnim() {
    var animFrameIndex by remember { mutableIntStateOf(0) }
    val anim = AssetsManager.TOKEN_ANIMATION

    LaunchedEffect(Unit) {
        while (true) {
            delay(anim.frameRate)
            animFrameIndex = Calculate.incrementAnimIndex(animFrameIndex, anim.frames.size)
        }
    }
    Box(modifier = Modifier.fillMaxHeight(0.8f), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = anim.frames[animFrameIndex]),
            contentDescription = null,
            modifier = Modifier.fillMaxHeight()
        )
    }
}
