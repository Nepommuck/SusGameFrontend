package edu.agh.susgame.front.gui.components.game.components.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.agh.susgame.R
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.front.gui.components.common.graph.edge.PathBuilder
import edu.agh.susgame.front.gui.components.common.graph.node.Host
import edu.agh.susgame.front.gui.components.common.graph.node.Node
import edu.agh.susgame.front.gui.components.common.theme.PaddingM
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.managers.GameManager

private val SIZE_DP = 50.dp

@Composable
fun NodeInfoComp(
    node: Node,
    onExit: () -> Unit,
    playerIdChangingPath: (PlayerId) -> Unit,
    pathBuilderState: PathBuilder,
    gameManager: GameManager
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomEnd)
                .padding(PaddingM)
                .background(Color(0x40808080))
        ) {

            Row(Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(5f)
                        .padding(PaddingM),
                ) {
                    Text(node.getInfo(), style = TextStyler.TerminalMedium)
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(PaddingM)
                ) {

                    Box(modifier = Modifier.size(SIZE_DP)) {
                        Image(
                            painter = painterResource(id = R.drawable.cross),
                            contentDescription = "Exit",
                            modifier = Modifier.clickable { onExit() }
                        )

                    }
                    val hostNode = node as? Host
                    hostNode?.let { host ->

                        Box(modifier = Modifier.size(SIZE_DP)) {
                            Image(
                                painter = painterResource(id = R.drawable.shuffle),
                                contentDescription = "Exit",
                                modifier = Modifier.clickable {
                                    gameManager.edges.forEach { (_, edge) -> edge.removePlayer(host.playerId) }
                                    playerIdChangingPath(host.playerId)
                                    pathBuilderState.addNodeToPath(nodeId = node.id)
                                    onExit()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
