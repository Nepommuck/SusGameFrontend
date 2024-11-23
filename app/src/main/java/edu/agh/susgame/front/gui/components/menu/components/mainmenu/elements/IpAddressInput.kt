package edu.agh.susgame.front.gui.components.menu.components.mainmenu.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.gui.components.common.theme.PaddingM
import edu.agh.susgame.front.service.web.IpAddressProvider

@Composable
fun IpAddressInput(ipAddressProvider: IpAddressProvider) {
    var currentInput by remember { mutableStateOf("") }
    var isInputValid by remember { mutableStateOf(true) }

    fun onIpValueChange(newValue: String) {
        currentInput = newValue
        isInputValid = IpAddressProvider.isValidIPv4Address(newValue)
    }

    fun onButtonClicked() {
        isInputValid = IpAddressProvider.isValidIPv4Address(currentInput)

        if (isInputValid) {
            ipAddressProvider.setIpAddress(currentInput)
        }
    }


    Row(
        modifier = Modifier.padding(top = PaddingM),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(PaddingM)

    ) {
        OutlinedTextField(
            label = {
                Text("EEEE")
            },
            placeholder = { Text("123.45.67.89") },
            value = currentInput,
            onValueChange = { onIpValueChange(it) },
            isError = !isInputValid,
            singleLine = true,
            readOnly = true,
        )

        Button(
            onClick = { onButtonClicked() },
        ) { Text("Click") }
    }
}
