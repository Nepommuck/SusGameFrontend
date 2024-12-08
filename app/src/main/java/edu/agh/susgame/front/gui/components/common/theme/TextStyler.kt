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
    private fun baseTextStyle(fontSize: TextUnit, color: Color = Color(0xF0989bb9)): TextStyle {
        return TextStyle(
            color = color,
            fontSize = fontSize,
            fontFamily = FontFamily(
                Font(R.font.vt323regular)
            ),
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.sp,
//            shadow = Shadow(
//                color = Color.Black,
//                offset = Offset(2f, 2f),
//                blurRadius = 4f
//            )
        )
    }

    val TerminalXXL = baseTextStyle(60.sp)
    val TerminalXL = baseTextStyle(40.sp)
    val TerminalL = baseTextStyle(30.sp)
    val TerminalM = baseTextStyle(23.sp)
    val TerminalS = baseTextStyle(14.sp)

    val Chat = baseTextStyle(16.sp, Color.Green)

    private val customFontFamily = FontFamily(
        Font(R.font.vt323regular)
    )

}