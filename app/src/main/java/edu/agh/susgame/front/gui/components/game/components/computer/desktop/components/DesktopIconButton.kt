package edu.agh.susgame.front.gui.components.game.components.computer.desktop.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.common.theme.PaddingS


private val BUTTON_SIZE = 80.dp

@Composable
fun DesktopIconButton(
    painter: Painter,
    imageDescription: String,
    onClick: () -> Unit,
    isVisible: () -> Boolean = { true },
) {
    if (!isVisible()) {
        DesktopIconPlaceholder()
    } else {
        Box(
            modifier = Modifier
                .size(BUTTON_SIZE)
                .clickable(role = Role.Button) { onClick() }
        ) {
            Image(
                painter = painter,
                contentDescription = imageDescription,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingS),
            )
        }
    }
}

@Composable
fun DesktopIconPlaceholder() {
    Box(modifier = Modifier.size(BUTTON_SIZE))
}
