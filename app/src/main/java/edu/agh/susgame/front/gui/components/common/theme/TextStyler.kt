package edu.agh.susgame.front.gui.components.common.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import edu.agh.susgame.R

object TextStyler {
    private fun baseTextStyle(
        fontSize: TextUnit,
        color: Color = Color(0xF0989bb9),
        letterSpacing: TextUnit = 3.sp
    ): TextStyle {
        return TextStyle(
            color = color,
            fontSize = fontSize,
            fontFamily = FontFamily(
                Font(R.font.vt323regular)
            ),
            fontWeight = FontWeight.Bold,
            letterSpacing = letterSpacing,
        )
    }

    val TerminalXXL = baseTextStyle(60.sp)
    val TerminalXL = baseTextStyle(40.sp)
    val TerminalL = baseTextStyle(23.sp)
    val TerminalM = baseTextStyle(17.sp)
    val TerminalS = baseTextStyle(14.sp)
    val TerminalXS = baseTextStyle(10.sp, color = Color.White.copy(alpha = 0.8f))
    val TerminalName = baseTextStyle(14.sp, letterSpacing = 1.sp)
    val TerminalInput = baseTextStyle(23.sp, color = Color.White.copy(alpha = 0.65f))
    val Chat = baseTextStyle(16.sp, Color.Green)
}