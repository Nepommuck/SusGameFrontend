package edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import edu.agh.susgame.R

@Composable
fun PlayerIcon(
    color: Color
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier, contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.player),
                contentDescription = "Player Color Icon",
                modifier = Modifier
                    .fillMaxSize(),
                colorFilter = ColorFilter.lighting(
                    multiply = color,
                    add = Color.Black
                )
            )
        }
    }
}