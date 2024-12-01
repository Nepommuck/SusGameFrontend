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
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.common.graph.node.Host
import edu.agh.susgame.front.gui.components.common.graph.node.Node
import edu.agh.susgame.front.gui.components.common.graph.node.Router
import edu.agh.susgame.front.gui.components.common.theme.PaddingM
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.managers.GameManager

private val SIZE_DP = 50.dp

@Composable
fun NodeInfoComp(
    node: Node,
    onExit: () -> Unit,
    changingPath: (Boolean) -> Unit,
    gameManager: GameManager
) {
    val isNodeHost: Boolean = node is Host
    val isNodeRouter: Boolean = node is Router
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
                    if (isNodeHost) {
                        val host = node as Host
                        Text(
                            Translation.Game.TOKENS + ": " + gameManager.playersById[host.playerId]?.tokens?.intValue.toString(),
                            style = TextStyler.TerminalMedium
                        )
                        if (node.id == gameManager.hostIdByPlayerId[gameManager.localPlayerId]) {
                            Slider(
                                value = host.packetsToSend.value.toFloat(),
                                onValueChange = { newValue ->
                                    gameManager.setHostFlow(
                                        hostId = host.id,
                                        flow = newValue.toInt()
                                    )
                                },
                                valueRange = 0f..host.maxPacketsToSend.value.toFloat(),
                                steps = host.maxPacketsToSend.value,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }

                    }
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
                    if (isNodeHost) {
                        val host = node as Host
                        if (gameManager.localPlayerId == host.playerId) {
                            Box(modifier = Modifier.size(SIZE_DP)) {
                                Image(
                                    painter = painterResource(id = R.drawable.shuffle),
                                    contentDescription = "Exit",
                                    modifier = Modifier.clickable {
                                        gameManager.clearEdgesLocal(gameManager.localPlayerId)
                                        changingPath(true)
                                        gameManager.addFirstNodeToPathBuilder(nodeId = host.id)
                                        onExit()
                                    }
                                )
                            }
                        }
                    }
                    if (isNodeRouter) {
                        val router = node as Router
                        Box(modifier = Modifier.size(SIZE_DP)) {
                            if (router.isOverloaded.value) {
                                Image(
                                    painter = painterResource(id = R.drawable.repair_tools),
                                    contentDescription = "Repair",
                                    modifier = Modifier.clickable {
                                        gameManager.repairRouter(router.id)
                                        onExit()
                                    }
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.plus),
                                    contentDescription = "Upgrade",
                                    modifier = Modifier.clickable {
                                        gameManager.upgradeRouter(router.id)
                                    }
                                )

                            }
                        }

                    }
                }
            }
        }
    }
}
