package edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.server

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import edu.agh.susgame.front.gui.components.common.graph.node.Server
import edu.agh.susgame.front.gui.components.common.theme.TextStyler

@Composable
fun ServerInfo(
    server: Server,
) {
    Text(text = server.getInfo(), style = TextStyler.TerminalS)
}