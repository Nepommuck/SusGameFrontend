package edu.agh.susgame.front.model.graph

import androidx.annotation.Nullable
import edu.agh.susgame.front.model.Player
import edu.agh.susgame.front.model.PlayerId
import edu.agh.susgame.front.util.Coordinates

class GameGraph(
    val nodes: MutableMap<NodeId, Node>,
    val edges: MutableMap<EdgeId, Edge>,
    val paths: MutableMap<PlayerId, Path>,
    val players: MutableMap<PlayerId, Player> = mutableMapOf(),
    val nodesToEdges: MutableMap<Pair<NodeId, NodeId>, EdgeId>,
    var serverId: NodeId = NodeId(0),
    val mapSize: Coordinates,
) {
    fun addNode(node: Node) {
        nodes[node.id] = node
    }

    fun addEdge(edge: Edge) {
        edges[edge.id] = edge
        nodesToEdges[Pair(edge.firstNodeId,edge.secondNodeId)] = edge.id
        nodesToEdges[Pair(edge.secondNodeId,edge.firstNodeId)] = edge.id
    }

}