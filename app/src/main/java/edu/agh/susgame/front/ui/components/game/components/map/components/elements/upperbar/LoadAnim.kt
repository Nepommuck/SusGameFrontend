package edu.agh.susgame.front.ui.components.game.components.map.components.elements.upperbar

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
import edu.agh.susgame.front.ui.components.common.theme.TextStyler
import edu.agh.susgame.front.ui.components.common.util.Calculate
import kotlinx.coroutines.delay

private const val delayMs: Long = 300
private val animationSymbols = listOf('/', "-", '\\', '|')

@Composable
fun LoadAnim() {
    var animFrameIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(delayMs)
            animFrameIndex = Calculate.incrementAnimIndex(animFrameIndex, animationSymbols.size)
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
            Text(text = animationSymbols[animFrameIndex].toString(), style = TextStyler.TerminalLarge)
        }
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
            Text(text = ")", style = TextStyler.TerminalLarge)
        }

    }
}