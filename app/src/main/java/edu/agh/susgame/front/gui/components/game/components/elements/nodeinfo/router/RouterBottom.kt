package edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.router

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.common.graph.node.Router
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.game.components.elements.RouterBar

@Composable
fun RouterBottom(
    router: Router,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        RouterBar(router = router, width = 1f, padding = 3.dp)
        Text(text = router.getBuffer(), style = TextStyler.TerminalS)
    }
}