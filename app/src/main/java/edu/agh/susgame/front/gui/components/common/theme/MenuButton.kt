package edu.agh.susgame.front.gui.components.common.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.theme.Transparent

@Composable
fun MenuButton(
    onClick: () -> Unit,
    text: String,
    alignment: Alignment = Alignment.Center
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .align(alignment)
        ) {
            Button(
                onClick = { onClick() },
                modifier = Modifier.requiredWidthIn(min = 120.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Transparent,
                    disabledContainerColor = Transparent
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = text, style = TextStyler.TerminalM)
            }
        }
    }
}