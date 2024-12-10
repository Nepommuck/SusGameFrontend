package edu.agh.susgame.front.gui.components.menu.components.createlobby.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation

@Composable
fun GameNameComp(
    gameName: String,
    onGameNameChange: (String) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            label = {
                Text(
                    text = Translation.CreateGame.ENTER_GAME_NAME,
                    style = TextStyler.TerminalS,
                )
            },
            value = gameName,
            textStyle = TextStyler.TerminalInput.copy(textAlign = TextAlign.Left),
            onValueChange = { onGameNameChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f),
            singleLine = true
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}