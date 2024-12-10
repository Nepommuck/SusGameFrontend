package edu.agh.susgame.front.gui.components.game.components.elements.upperbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import edu.agh.susgame.R

@Composable
fun MenuButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = R.drawable.menu),
            contentDescription = null,
            modifier = Modifier
                .clickable { onClick() }
        )
    }
}
