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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.common.graph.node.Host
import edu.agh.susgame.front.gui.components.common.graph.node.Node
import edu.agh.susgame.front.gui.components.common.graph.node.Router
import edu.agh.susgame.front.gui.components.common.graph.node.Server
import edu.agh.susgame.front.gui.components.common.theme.PaddingM
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.AssetsManager
import edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.host.HostBottom
import edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.host.HostIcons
import edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.host.HostInfo
import edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.router.RouterBottom
import edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.router.RouterIcons
import edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.router.RouterInfo
import edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.server.ServerBottom
import edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.server.ServerIcons
import edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo.server.ServerInfo
import edu.agh.susgame.front.managers.GameManager

private val SIZE_DP = 50.dp

@Composable
fun NodeInfoComp(
    node: Node,
    onExit: () -> Unit,
    gameManager: GameManager
) {

    val resourceId = AssetsManager.nodeToResourceId(node)

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.55f)
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
                    .alpha(0.03f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(40.dp)),
                contentScale = ContentScale.Crop
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
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(node.getNodeName(), style = TextStyler.TerminalM)
                    Image(
                        painter = painterResource(id = R.drawable.cross),
                        contentDescription = "Exit",
                        modifier = Modifier
                            .clickable { onExit() }
                            .padding(6.dp)
                            .alpha(0.8f)
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
                            .weight(3f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(2f)
                        ) {
                            when (node) {
                                is Host -> HostInfo(host = node, gameManager)
                                is Router -> RouterInfo(router = node, gameManager)
                                is Server -> ServerInfo(server = node)
                            }
                        }


                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            when (node) {
                                is Host -> HostBottom(host = node, gameManager = gameManager)
                                is Router -> RouterBottom(router = node)
                                is Server -> ServerBottom(server = node)
                            }

                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(4f)
                    ) {
                        when (node) {
                            is Host -> HostIcons(
                                host = node,
                                gameManager = gameManager,
                                onExit = onExit
                            )

                            is Router -> RouterIcons(router = node, gameManager = gameManager)
                            is Server -> ServerIcons(server = node, gameManager = gameManager)
                        }

                    }
                }
            }
        }
    }
}

