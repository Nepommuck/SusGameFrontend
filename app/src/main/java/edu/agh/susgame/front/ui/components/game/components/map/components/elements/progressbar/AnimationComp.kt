package edu.agh.susgame.front.ui.components.game.components.map.components.elements.progressbar

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun AnimationComp() {
    val symbols = listOf('/', "-", '\\', '|')
    var index by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(300) // Opóźnienie dla animacji
            index = (index + 1) % symbols.size // Zmieniaj indeks
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
            Text(text = "(")
        }
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
            Text(text = symbols[index].toString())
        }
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
            Text(text = ")")
        }

    }
}