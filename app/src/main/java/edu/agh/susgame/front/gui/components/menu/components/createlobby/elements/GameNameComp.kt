package edu.agh.susgame.front.gui.components.menu.components.createlobby.elements

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
    OutlinedTextField(
        label = {
            Text(
                text = Translation.CreateGame.ENTER_GAME_NAME,
                style = TextStyler.TerminalS,
            )
        },
        value = gameName,
        textStyle = TextStyler.TerminalM.copy(textAlign = TextAlign.Center),
        onValueChange = { onGameNameChange(it) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}