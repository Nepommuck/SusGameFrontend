package edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation

@Composable
fun JoinButton(
    isJoinButtonLoading: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.padding(8.dp),
        onClick = onClick,
        enabled = isEnabled && !isJoinButtonLoading,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Green.copy(alpha = 0.3f),
            disabledContainerColor = Color.LightGray.copy(alpha = 0.3f),
        )
    ) {
        Text(
            text = if (isJoinButtonLoading) Translation.Button.LOADING else Translation.Button.JOIN,
            style = TextStyler.TerminalL,
            color = Color.White
        )
    }
}
