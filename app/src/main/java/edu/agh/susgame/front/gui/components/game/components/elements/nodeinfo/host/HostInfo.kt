package edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.host

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import edu.agh.susgame.front.gui.components.common.graph.node.Host
import edu.agh.susgame.front.gui.components.common.graph.node.Node
import edu.agh.susgame.front.gui.components.common.theme.TextStyler

@Composable
fun HostInfo(
    host: Host,
) {

    Text(text = host.getInfo(), style = TextStyler.TerminalS)

}