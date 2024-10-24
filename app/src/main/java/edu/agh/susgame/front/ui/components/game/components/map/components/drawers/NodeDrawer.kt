package edu.agh.susgame.front.ui.components.game.components.map.components.drawers

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.agh.susgame.R
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.front.model.graph.GameGraph
import edu.agh.susgame.front.model.graph.Host
import edu.agh.susgame.front.model.graph.NodeId
import edu.agh.susgame.front.model.graph.PathBuilder
import edu.agh.susgame.front.model.graph.Router
import edu.agh.susgame.front.model.graph.Server


private const val scaleFactor = 0.1f
private const val size: Int = 1024

@Composable
fun NodeDrawer(
    gameGraph: GameGraph,
    playerIdChangingPath: PlayerId?,
    pathBuilderState: PathBuilder,
    onInspectedNodeChange: (NodeId?) -> Unit,
) {

    gameGraph.nodes.forEach { (key, node) ->

        val imageResource = when (node) {
            is Host -> R.drawable.host
            is Router -> R.drawable.router
            is Server -> R.drawable.server
            else -> R.drawable.host
        }

        val density = LocalDensity.current

        val positionX = with(density) { node.position.x.dp.toPx() }
        val positionY = with(density) { node.position.y.dp.toPx() }

        Image(
            painter = painterResource(id = imageResource),
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer(
                    translationX = positionX - size / 2,
                    translationY = positionY - size / 2,
                    scaleX = scaleFactor,
                    scaleY = scaleFactor
                )
                .clickable {

                    playerIdChangingPath?.let {
                        addNodeToPath(
                            nodeId = node.id,
                            pathBuilderState = pathBuilderState,
                            playerId = playerIdChangingPath,
                            gameGraph = gameGraph
                        )


                    }
                    onInspectedNodeChange(node.id)
                }
        )
    }

}

private fun addNodeToPath(
    nodeId: NodeId,
    pathBuilderState: PathBuilder,
    playerId: PlayerId,
    gameGraph: GameGraph,
) {
    val edgeId = pathBuilderState.path.lastOrNull()?.let { lastNode ->
        gameGraph.getEdgeId(lastNode, nodeId)
    }

    edgeId?.let {
        if (pathBuilderState.isNodeValid(nodeId)) {
            pathBuilderState.addNodeToPath(nodeId)
            gameGraph.edges[edgeId]?.addPlayer(playerId)
        }
    }

}
