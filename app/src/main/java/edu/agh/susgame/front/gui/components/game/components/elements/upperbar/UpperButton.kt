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
fun UpperButton(
    onClick: () -> Unit,
    resourceId: Int
) {
    Box(
        modifier = Modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = resourceId),
            contentDescription = null,
            modifier = Modifier
                .clickable { onClick() }
        )
    }
}
