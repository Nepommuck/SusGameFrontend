package edu.agh.susgame.front.gui.components.game.components.elements.upperbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import edu.agh.susgame.R

@Composable
fun HourglassImg() {
    Box(modifier = Modifier.fillMaxHeight(0.8f), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.hourglass),
            contentDescription = null,
            modifier = Modifier.fillMaxHeight(),
            colorFilter = ColorFilter.tint(Color.LightGray.copy(alpha = 0.8f))
        )
    }
}