package edu.agh.susgame.front.ui.component.menu.components.createlobby.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.ui.Translation

@Composable
fun GameNameComp(
    gameName: String,
    onGameNameChange: (String) -> Unit
) {
//    var gameName by remember { mutableStateOf(Translation.CreateGame.DEFAULT_GAME_NAME) }
    OutlinedTextField(
        label = {
            Text(
                text = Translation.CreateGame.ENTER_GAME_NAME + ": "
            )
        },
        value = gameName,
        onValueChange = { onGameNameChange(it) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

}