package edu.agh.susgame.front.gui.components.common.theme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun Header(title: String, color: Color = Color.Black) {
    Text(
        text = title,
        style = TextStyler.TerminalXXL
    )
}
