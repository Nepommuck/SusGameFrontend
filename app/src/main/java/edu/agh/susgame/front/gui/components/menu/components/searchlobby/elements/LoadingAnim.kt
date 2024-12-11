package edu.agh.susgame.front.gui.components.menu.components.searchlobby.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.common.util.AssetsManager
import kotlinx.coroutines.delay


@Composable
fun LoadingAnim() {
    val currentFrame = remember { mutableIntStateOf(0) }
    val animationFrames = AssetsManager.LOADING_ANIMATION.frames
    val frameRate = AssetsManager.LOADING_ANIMATION.frameRate

    LaunchedEffect(Unit) {
        while (true) {
            currentFrame.intValue = (currentFrame.intValue + 1) % animationFrames.size
            delay(frameRate)
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .requiredSize(100.dp)
                .padding(16.dp)
                .align(Alignment.TopEnd)
        ) {
            Image(
                painter = painterResource(id = animationFrames[currentFrame.intValue]),
                contentDescription = "Loading Animation",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
