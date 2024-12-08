package edu.agh.susgame.front.gui.components.menu.components.searchlobby.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.common.theme.Header
import edu.agh.susgame.front.gui.components.common.theme.TextStyler

@Composable
fun HeaderLobby(
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Header(title = text, style = TextStyler.TerminalXL)
        }
    }
}