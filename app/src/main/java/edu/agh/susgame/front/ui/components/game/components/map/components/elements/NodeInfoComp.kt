package edu.agh.susgame.front.ui.components.game.components.map.components.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.front.Translation
import edu.agh.susgame.front.model.graph.GameGraph
import edu.agh.susgame.front.model.graph.nodes.Host
import edu.agh.susgame.front.model.graph.Node
import edu.agh.susgame.front.model.graph.PathBuilder
import edu.agh.susgame.front.ui.components.common.theme.PaddingM

@Composable
fun NodeInfoComp(
    node: Node,
    onExit: () -> Unit,
    playerIdChangingPath: (PlayerId) -> Unit,
    pathBuilderState: PathBuilder,
    mapState: GameGraph
) {
    Box(
        modifier = Modifier
            .background(Color.Cyan)
            .padding(PaddingM),
    ) {
        Row {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(node.getInfo())
            }
            Column {
                Button(onClick = { onExit() }) {
                    Text("X")
                }

                val hostNode = node as? Host
                hostNode?.let { host ->
                    Button(onClick = {
                        mapState.edges.forEach { (_, edge) -> edge.removePlayer(host.playerId) }
                        playerIdChangingPath(host.playerId)
                        pathBuilderState.addNodeToPath(nodeId = node.id)
                        onExit()
                    }) {
                        Text(Translation.Game.CHANGE_PATH)
                    }
                    mapState.paths[host.playerId]?.let { Text(it.getPathString()) }
                }
            }
        }
    }
}