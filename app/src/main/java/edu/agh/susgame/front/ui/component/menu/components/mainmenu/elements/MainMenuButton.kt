package edu.agh.susgame.front.ui.component.menu.components.mainmenu.elements

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainMenuButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick,
        modifier = Modifier.defaultMinSize(minWidth = 160.dp),
    ) {
        Text(text)
    }
}