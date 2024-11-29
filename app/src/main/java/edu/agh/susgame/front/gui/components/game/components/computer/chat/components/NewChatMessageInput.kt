package edu.agh.susgame.front.gui.components.game.components.computer.chat.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.agh.susgame.front.gui.components.common.theme.PaddingS


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
            focusedTextColor = Color.Green,
            unfocusedTextColor = Color.Green,
            cursorColor = Color.Green,
            focusedIndicatorColor = Color.Green,
            unfocusedIndicatorColor = Color.Green,
        )
    )
}
