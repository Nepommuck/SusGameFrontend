package edu.agh.susgame.front.gui.components.game.components.computer.desktop.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp


private val BUTTON_SIZE = 80.dp

@Composable
fun DesktopIconButton(
    painter: Painter,
    imageDescription: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(BUTTON_SIZE)
            .clickable(role = Role.Button) { onClick() }
    ) {
        Image(
            painter = painter,
            contentDescription = imageDescription,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun DesktopIconMock() {
    Box(modifier = Modifier.size(BUTTON_SIZE))
}
