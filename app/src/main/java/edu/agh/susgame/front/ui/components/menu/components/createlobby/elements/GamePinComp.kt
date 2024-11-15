package edu.agh.susgame.front.ui.components.menu.components.createlobby.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import edu.agh.susgame.front.config.Config
import edu.agh.susgame.front.ui.components.common.util.Translation

@Composable
fun GamePinComp(
    gamePin: String,
    onGamePinChange: (String) -> Unit,
    showPassword: Boolean,
    onShowPasswordChange: (Boolean) -> Unit,
    isGamePinEnabled: Boolean,
    onGamePinEnabledChange: (Boolean) -> Unit
){
    Row(
        Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedTextField(
            label = {
                Text(
                    Translation.CreateGame.ENTER_GAME_PIN
                )
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
            onValueChange = { if (it.length <= Config.gameConfig.maxPinLength) onGamePinChange(it) },
            singleLine = true,
            enabled = isGamePinEnabled,
            modifier = Modifier.weight(1f),
        )
        Checkbox(
            checked = isGamePinEnabled,
            onCheckedChange = {
                onGamePinEnabledChange(it)
                if (!it) onGamePinChange("")
            }
        )
    }

}