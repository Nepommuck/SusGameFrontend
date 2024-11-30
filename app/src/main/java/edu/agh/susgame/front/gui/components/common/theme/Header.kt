package edu.agh.susgame.front.gui.components.common.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em

@Composable
fun Header(title: String, color: Color = Color.Black) {
    Text(
        text = title,
        fontSize = 6.em,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = PaddingL)
    )
}
