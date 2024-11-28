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
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Calculate
import kotlinx.coroutines.delay

private const val DELAY_MS: Long = 300
private val ANIMATION_SYMBOLS = listOf('/', "-", '\\', '|')

@Composable
fun LoadAnim() {
    var animFrameIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(DELAY_MS)
            animFrameIndex = Calculate.incrementAnimIndex(animFrameIndex, ANIMATION_SYMBOLS.size)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
            Text(text = "(", style = TextStyler.TerminalLarge)
        }
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
            Text(
                text = ANIMATION_SYMBOLS[animFrameIndex].toString(),
                style = TextStyler.TerminalLarge
            )
        }
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
            Text(text = ")", style = TextStyler.TerminalLarge)
        }

    }
}