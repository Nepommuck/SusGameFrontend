package edu.agh.susgame.front.gui.components.menu.components.createlobby.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import edu.agh.susgame.front.config.AppConfig
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation

@Composable
fun GamePinComp(
    gamePin: String,
    showPassword: Boolean,
    isGamePinEnabled: Boolean,
    onGamePinChange: (String) -> Unit,
    onShowPasswordChange: (Boolean) -> Unit,
    onGamePinEnabledChange: (Boolean) -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedTextField(
            label = {
                Text(text = Translation.CreateGame.ENTER_GAME_PIN, style= TextStyler.TerminalS)
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                if (isGamePinEnabled) {
                    IconButton(onClick = { onShowPasswordChange(!showPassword) }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "toggle_password_visibility"
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            value = gamePin,
            onValueChange = { if (it.length <= AppConfig.gameConfig.maxPinLength) onGamePinChange(it) },
            singleLine = true,
            enabled = isGamePinEnabled,
            modifier = Modifier.weight(4f),
            textStyle = TextStyler.TerminalInput
        )
        Checkbox(
            modifier = Modifier.weight(1f),
            checked = isGamePinEnabled,
            onCheckedChange = {
                onGamePinEnabledChange(it)
                if (!it) onGamePinChange("")
            },
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Green.copy(alpha = 0.5f),
                uncheckedColor = Color.White.copy(alpha = 0.7f),
                checkmarkColor = Color.White.copy(alpha = 0.7f)
            )
        )
    }
}