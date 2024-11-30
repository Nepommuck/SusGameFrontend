package edu.agh.susgame.front.gui.components.game.components.computer.chat.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.game.components.computer.chat.ChatColors


@Composable
fun ChatButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors().copy(
            contentColor = ChatColors.TEXT,
            containerColor = ChatColors.BACKGROUND,
        ),
        border = BorderStroke(2.dp, ChatColors.TEXT),
    ) {
        Text(text)
    }
}
