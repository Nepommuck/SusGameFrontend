package edu.agh.susgame.front.ui.components.game.components.map.components.drawers

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import edu.agh.susgame.R
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.front.model.graph.GameGraph
import edu.agh.susgame.front.model.graph.NodeId
import edu.agh.susgame.front.model.graph.PathBuilder
import edu.agh.susgame.front.model.graph.nodes.Host
import edu.agh.susgame.front.model.graph.nodes.Router
import edu.agh.susgame.front.model.graph.nodes.Server


private const val scaleFactor = 0.04f

@Composable
fun NodeDrawer(
    gameGraph: GameGraph,
    playerIdChangingPath: PlayerId?,
    pathBuilderState: PathBuilder,
    onInspectedNodeChange: (NodeId?) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val context = LocalContext.current
        gameGraph.nodes.forEach { (key, node) ->

            val imageResource = when (node) {
                is Host -> R.drawable.host
                is Router -> R.drawable.router
                is Server -> R.drawable.server
                else -> R.drawable.host
            }

            val density = LocalDensity.current

            val imageSize = getImageSize(context, imageResource)
            val width = with(density) { imageSize.width.dp.toPx() }
            val height = with(density) { imageSize.height.dp.toPx() }

            val positionX = with(density) { node.position.x.dp.toPx() }
            val positionY = with(density) { node.position.y.dp.toPx() }


            Box(modifier = Modifier
                .size(width = with(density) { width.toDp() } * scaleFactor,
                    height = with(density) { height.toDp() } * scaleFactor)
                .graphicsLayer(
                    translationX = positionX - (width * scaleFactor) / 2,
                    translationY = positionY - (height * scaleFactor) / 2

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
                }) {
                Image(
                    painter = painterResource(id = imageResource),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

fun getImageSize(context: Context, resId: Int): IntSize {
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeResource(context.resources, resId, options)
    return IntSize(options.outWidth, options.outHeight)
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
