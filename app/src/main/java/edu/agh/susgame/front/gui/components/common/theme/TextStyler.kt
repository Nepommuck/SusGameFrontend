package edu.agh.susgame.front.gui.components.common.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

object TextStyler {
    private fun baseTextStyle(fontSize: TextUnit): TextStyle {
        return TextStyle(
            color = Color.Gray,
            fontSize = fontSize,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            shadow = Shadow(
                color = Color.Black,
                offset = Offset(2f, 2f),
                blurRadius = 4f
            )
        )
    }

    val TerminalLarge = baseTextStyle(14.sp)
    val TerminalMedium = baseTextStyle(11.sp)
}