package edu.agh.susgame.front.ui.components.common.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object TextStyler {
    val TerminalLarge = TextStyle(
        color = Color.Gray,
        fontSize = 14.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,          // Grubość czcionki, np. Normal, Medium, Bold
        letterSpacing = 1.5.sp,                // Odstęp między literami
//        textAlign = TextAlign.Center,          // Wyrównanie tekstu, np. Left, Center, Right, Justify
        shadow = Shadow(                       // Cień tekstu
            color = Color.Black,
            offset = Offset(2f, 2f),
            blurRadius = 4f
        ),
    )
    val TerminalMedium = TextStyle(
        color = Color.Gray,
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,          // Grubość czcionki, np. Normal, Medium, Bold
        letterSpacing = 1.5.sp,                // Odstęp między literami
//        textAlign = TextAlign.Center,          // Wyrównanie tekstu, np. Left, Center, Right, Justify
        shadow = Shadow(                       // Cień tekstu
            color = Color.Black,
            offset = Offset(2f, 2f),
            blurRadius = 4f
        ),
    )


}