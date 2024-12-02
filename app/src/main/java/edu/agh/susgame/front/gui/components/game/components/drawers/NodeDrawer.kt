package edu.agh.susgame.front.gui.components.game.components.drawers

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.common.graph.node.Host
import edu.agh.susgame.front.gui.components.common.graph.node.Node
import edu.agh.susgame.front.gui.components.common.graph.node.NodeId
import edu.agh.susgame.front.gui.components.common.graph.node.Router
import edu.agh.susgame.front.gui.components.common.graph.node.Server
import edu.agh.susgame.front.managers.GameManager


private const val SCALE_FACTOR = 0.04f

@Composable
fun NodeDrawer(
    gameManager: GameManager,
    changingPath: Boolean,
    onInspectedNodeChange: (NodeId?) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val context = LocalContext.current
        gameManager.nodesById.forEach { (_, node) ->

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
                    if (changingPath) {
                        gameManager.addNodeToPathBuilder(node.id)
                    } else {
                        onInspectedNodeChange(node.id)
                    }
                }) {
                Image(
                    painter = painterResource(id = imageResourceId),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    colorFilter = getColorFilterForNode(node, gameManager)
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

private fun nodeToResourceId(node: Node): Int = when (node) {
    is Host -> R.drawable.host
    is Router -> R.drawable.router
    is Server -> R.drawable.server
}

fun getColorFilterForNode(
    node: Node,
    gameManager: GameManager
): ColorFilter? {
    return when (node) {
        is Host -> {
            val hostColor = gameManager.playersById[node.playerId]?.color?.value ?: Color.Gray
            ColorFilter.tint(color = hostColor.copy(alpha = 0.8f))
        }

        is Router -> {
            val load = if (node.overheatLevel.value>0)
                    node.overheatLevel.value.toFloat() / gameManager.criticalBufferOverheatLevel
                else
                    0f

            val routerColor = if (!node.isWorking.value)
                    Color.Gray
                else
                    Color(
                        red = (load * 255).toInt(),
                        green = ((1 - load) * 255).toInt(),
                        blue = 0,
                        alpha = (0.8f * 255).toInt()
                    )
            ColorFilter.tint(color = routerColor)
        }
        else -> null
    }
}