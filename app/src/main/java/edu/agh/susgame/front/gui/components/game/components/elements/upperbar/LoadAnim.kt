package edu.agh.susgame.front.gui.components.game.components.elements.upperbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.gui.components.common.theme.Animations
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Calculate
import kotlinx.coroutines.delay

@Composable
fun LoadAnim() {
    var animFrameIndex by remember { mutableIntStateOf(0) }
    val anim = Animations.HOURGLASS_ANIMATION
    LaunchedEffect(Unit) {
        while (true) {
            delay(anim.frameRate)
            animFrameIndex = Calculate.incrementAnimIndex(animFrameIndex, anim.frames.size)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
            Text(text = "(", style = TextStyler.TerminalM)
        }
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
            Text(
                text = anim.frames[animFrameIndex],
                style = TextStyler.TerminalM
            )
        }
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
            Text(text = ")", style = TextStyler.TerminalM)
        }

    }
}