package edu.agh.susgame.front.gui.components.game.components.computer.chat.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.gui.components.common.theme.PaddingS
import edu.agh.susgame.front.gui.components.game.components.computer.chat.ChatColors


@Composable
fun NewChatMessageInput(
    inputValue: MutableState<String>,
) {
    OutlinedTextField(
        modifier = Modifier
            .padding(end = PaddingS)
            .fillMaxWidth(),
        value = inputValue.value,
        onValueChange = { inputValue.value = it },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors().copy(
            focusedTextColor = ChatColors.TEXT,
            unfocusedTextColor = ChatColors.TEXT,
            cursorColor = ChatColors.TEXT,
            focusedIndicatorColor = ChatColors.TEXT,
            unfocusedIndicatorColor = ChatColors.TEXT,
        )
    )
}
