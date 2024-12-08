package edu.agh.susgame.front.gui.components.common.theme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun Header(title: String, style: TextStyle = TextStyler.TerminalS) {
    Text(
        text = title,
        style = style
    )
}
