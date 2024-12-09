package edu.agh.susgame.front.gui.components.menu.components.mainmenu.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.config.AppConfig
import edu.agh.susgame.front.gui.components.common.theme.PaddingM
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.theme.Transparent
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.service.web.IpAddressProvider


private const val MAX_LENGTH = "255.255.255.255".length
private val ALLOWED_CHARS = ('0'..'9').toSet() + '.'


@Composable
fun IpAddressInput(ipAddressProvider: IpAddressProvider) {
    val inputInitialValue = ipAddressProvider.getCurrentIpAddress()
        ?: AppConfig.webConfig.defaultIpAddress

    var currentInput by remember { mutableStateOf(inputInitialValue.orEmpty()) }
    var isInputValid by remember { mutableStateOf(true) }
    var isInEditMode by remember {
        mutableStateOf(inputInitialValue == null)
    }

    fun onIpValueChange(newValue: String) {
        if (newValue.length > MAX_LENGTH || !newValue.all { char -> ALLOWED_CHARS.contains(char) }) {
            isInputValid = false
        } else {
            currentInput = newValue
            isInputValid = IpAddressProvider.isValidIPv4Address(newValue)
        }
    }

    fun onButtonClicked() {
        if (!isInEditMode) {
            isInEditMode = true
            ipAddressProvider.setEmptyIpAddress()
        } else {
            isInputValid = IpAddressProvider.isValidIPv4Address(currentInput)

            if (isInputValid) {
                isInEditMode = false
                ipAddressProvider.setIpAddress(currentInput)
            }
        }
    }

    Row(
        modifier = Modifier.padding(top = PaddingM),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(PaddingM)

    ) {
        OutlinedTextField(
            label = {
                Text(
                    Translation.Menu.IP_ADDRESS,
                    style = TextStyler.TerminalS,
                    textAlign = TextAlign.Left
                )
            },
            placeholder = { Text("123.45.67.89") },
            value = currentInput,
            onValueChange = { onIpValueChange(it) },
            isError = !isInputValid,
            singleLine = true,
            readOnly = !isInEditMode,
            textStyle = TextStyler.TerminalInput.copy(textAlign = TextAlign.Center),
            modifier = Modifier.wrapContentWidth()
        )
        Button(
            onClick = { onButtonClicked() },
            modifier = Modifier.requiredWidthIn(min = 120.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Transparent,
                disabledContainerColor = Transparent
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = if (isInEditMode) Translation.Button.ACCEPT else Translation.Button.EDIT,
                style = TextStyler.TerminalM
            )
        }

    }
}
