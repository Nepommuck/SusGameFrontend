package edu.agh.susgame.front.gui.components.menu.components.mainmenu.elements

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.service.web.IpAddressProvider

@Composable
fun MainMenuButton(
    text: String,
    onClick: () -> Unit,
    ipAddressProvider: IpAddressProvider,
) {
    val isIpAddressDefined by ipAddressProvider.isIpAddressDefined.collectAsState()

    Button(
        onClick,
        modifier = Modifier.defaultMinSize(minWidth = 160.dp).scale(1.3f),
        enabled = isIpAddressDefined,
    ) {
        Text(text)
    }
}
