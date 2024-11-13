package edu.agh.susgame.front.ui.components.game.map.components.drawers

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
import edu.agh.susgame.front.ui.graph.GameManager
import edu.agh.susgame.front.ui.graph.PathBuilder
import edu.agh.susgame.front.ui.graph.node.Host
import edu.agh.susgame.front.ui.graph.node.Node
import edu.agh.susgame.front.ui.graph.node.NodeId
import edu.agh.susgame.front.ui.graph.node.Router
import edu.agh.susgame.front.ui.graph.node.Server


private const val SCALE_FACTOR = 0.04f

@Composable
fun NodeDrawer(
    gameManager: GameManager,
    playerIdChangingPath: PlayerId?,
    pathBuilderState: PathBuilder,
    onInspectedNodeChange: (NodeId?) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val context = LocalContext.current
        gameManager.nodes.forEach { (_, node) ->

            val imageResourceId = nodeToResourceId(node)

            val density = LocalDensity.current

            val imageSize = getImageSize(context, imageResourceId)
            val width = with(density) { imageSize.width.dp.toPx() }
            val height = with(density) { imageSize.height.dp.toPx() }

            val positionX = with(density) { node.position.x.dp.toPx() }
            val positionY = with(density) { node.position.y.dp.toPx() }


            Box(modifier = Modifier
                .size(width = with(density) { width.toDp() } * SCALE_FACTOR,
                    height = with(density) { height.toDp() } * SCALE_FACTOR)
                .graphicsLayer(
                    translationX = positionX - (width * SCALE_FACTOR) / 2,
                    translationY = positionY - (height * SCALE_FACTOR) / 2

                )
                .clickable {

                    playerIdChangingPath?.let {
                        addNodeToPath(
                            nodeId = node.id,
                            pathBuilderState = pathBuilderState,
                            playerId = playerIdChangingPath,
                            gameManager = gameManager
                        )
                    }
                    onInspectedNodeChange(node.id)
                }) {
                Image(
                    painter = painterResource(id = imageResourceId),
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
    gameManager: GameManager,
) {
    val edgeId = pathBuilderState.path.lastOrNull()?.let { lastNode ->
        gameManager.getEdgeId(lastNode, nodeId)
    }

    edgeId?.let {
        if (pathBuilderState.isNodeValid(nodeId)) {
            pathBuilderState.addNodeToPath(nodeId)
            gameManager.edges[edgeId]?.addPlayer(playerId)
        }
    }

}

private fun nodeToResourceId(node: Node): Int = when (node) {
    is Host -> R.drawable.host
    is Router -> R.drawable.router
    is Server -> R.drawable.server
}
