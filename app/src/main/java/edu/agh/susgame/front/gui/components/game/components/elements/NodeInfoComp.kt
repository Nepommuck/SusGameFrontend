package edu.agh.susgame.front.gui.components.game.components.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.common.graph.node.Host
import edu.agh.susgame.front.gui.components.common.graph.node.Node
import edu.agh.susgame.front.gui.components.common.theme.PaddingM
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.AssetsManager
import edu.agh.susgame.front.managers.GameManager

private val SIZE_DP = 50.dp

@Composable
fun NodeInfoComp(
    node: Node,
    onExit: () -> Unit,
    changingPath: (Boolean) -> Unit,
    gameManager: GameManager
) {

    val resourceId = AssetsManager.nodeToResourceId(node)

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomEnd)
                .padding(PaddingM)
                .clip(RoundedCornerShape(40.dp))
                .border(6.dp, Color.Black.copy(alpha = 0.8f), RoundedCornerShape(40.dp))
                .background(Color(0x40808080))
        ) {
            Image(
                painter = painterResource(
                    id = resourceId
                ),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .alpha(0.07f)
                    .fillMaxHeight(0.8f)
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth(0.85f)
                    .align(Alignment.Center),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color.Gray),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(node.getNodeName(), style = TextStyler.TerminalM)
                    Image(
                        painter = painterResource(id = R.drawable.cross),
                        contentDescription = "Exit",
                        modifier = Modifier
                            .clickable { onExit() }
                            .padding(4.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(3f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .background(Color.LightGray)
                    ) {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .weight(2f)
                            .background(Color.Red)) {

                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                                .background(Color.Cyan),
                            contentAlignment = Alignment.Center
                        ) {
                            if (node is Host && node.id == gameManager.hostIdByPlayerId[gameManager.localPlayerId]) {
                                Slider(
                                    value = node.packetsToSend.intValue.toFloat(),
                                    onValueChange = { newValue ->
                                        gameManager.handleHostFlowChange(
                                            hostId = node.id,
                                            flow = newValue.toInt()
                                        )
                                    },
                                    valueRange = 0f..node.maxPacketsToSend.intValue.toFloat(),
                                    steps = node.maxPacketsToSend.intValue + 1,
//                                    modifier = Modifier.padding(top = 16.dp)
                                )
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .background(Color.Blue)
                    ) {
                        Text(text = "2")
                    }
                }


//                Row(Modifier.fillMaxSize()) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .weight(5f)
//                            .padding(PaddingM),
//                    ) {
////                        Text(node.getInfo(), style = TextStyler.TerminalS)
//                        if (node is Host) {
//                            Text(
//                                Translation.Game.TOKENS + ": " + gameManager.playersById[node.playerId]?.tokens?.intValue.toString(),
//                                style = TextStyler.TerminalS
//                            )
//                            if (node.id == gameManager.hostIdByPlayerId[gameManager.localPlayerId]) {
//                                Slider(
//                                    value = node.packetsToSend.intValue.toFloat(),
//                                    onValueChange = { newValue ->
//                                        gameManager.handleHostFlowChange(
//                                            hostId = node.id,
//                                            flow = newValue.toInt()
//                                        )
//                                    },
//                                    valueRange = 0f..node.maxPacketsToSend.intValue.toFloat(),
//                                    steps = node.maxPacketsToSend.intValue + 1,
//                                    modifier = Modifier.padding(top = 16.dp)
//                                )
//                            }
//                        }
//                    }
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .weight(1f)
//                            .padding(PaddingM)
//                    ) {
//
////                    Box(modifier = Modifier.size(SIZE_DP)) {
////                        Image(
////                            painter = painterResource(id = R.drawable.cross),
////                            contentDescription = "Exit",
////                            modifier = Modifier.clickable { onExit() }
////                        )
////
////                    }
//                        when (node) {
//                            is Host -> {
//                                if (gameManager.localPlayerId == node.playerId) {
//                                    Box(modifier = Modifier.size(SIZE_DP)) {
//                                        Image(
//                                            painter = painterResource(id = R.drawable.shuffle),
//                                            contentDescription = "Exit",
//                                            modifier = Modifier.clickable {
//                                                gameManager.clearEdges(gameManager.localPlayerId)
//                                                changingPath(true)
//                                                gameManager.addNodeToPathBuilder(nodeId = node.id)
//                                                onExit()
//                                            }
//                                        )
//                                    }
//                                }
//                            }
//
//                            is Router -> {
//                                Box(modifier = Modifier.size(SIZE_DP)) {
//                                    if (!node.isWorking.value) {
//                                        Image(
//                                            painter = painterResource(id = R.drawable.repair_tools),
//                                            contentDescription = "Repair",
//                                            modifier = Modifier.clickable {
//                                                gameManager.handleRouterRepair(node.id)
//                                                onExit()
//                                            }
//                                        )
//                                    } else {
//                                        Image(
//                                            painter = painterResource(id = R.drawable.plus),
//                                            contentDescription = "Upgrade",
//                                            modifier = Modifier.clickable {
//                                                gameManager.handleRouterUpgrade(node.id)
//                                            }
//                                        )
//
//                                    }
//                                }
//                            }
//
//                            is Server -> {}
//                        }
//                    }
//                }
            }
        }
    }
}
