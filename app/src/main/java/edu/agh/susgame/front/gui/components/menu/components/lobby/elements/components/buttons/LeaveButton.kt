package edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation

@Composable
fun LeaveButton(
    onClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Button(
                onClick = { onClick() },
                modifier = Modifier.wrapContentSize(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0),
                    disabledContainerColor = Color(0)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = Translation.Button.LEAVE, style = TextStyler.TerminalM)
            }
        }
    }
}