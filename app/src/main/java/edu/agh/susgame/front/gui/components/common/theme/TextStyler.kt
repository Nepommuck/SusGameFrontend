package edu.agh.susgame.front.gui.components.common.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

object TextStyler {
    private fun baseTextStyle(fontSize: TextUnit): TextStyle {
        return TextStyle(
            color = Color.LightGray.copy(alpha = 0.8f),
            fontSize = fontSize,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
//            shadow = Shadow(
//                color = Color.Black,
//                offset = Offset(2f, 2f),
//                blurRadius = 4f
//            )
        )
    }
    val TerminalXXL = baseTextStyle(50.sp)
    val TerminalL = baseTextStyle(20.sp)
    val TerminalM = baseTextStyle(14.sp)
    val TerminalS = baseTextStyle(11.sp)
}