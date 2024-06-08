package edu.agh.susgame.front.model.graph

import edu.agh.susgame.front.model.Player
import edu.agh.susgame.front.model.PlayerId
import edu.agh.susgame.front.util.Coordinates

class GameGraph(
    val nodes: MutableMap<NodeId, Node>,
    val edges: MutableMap<EdgeId, Edge>,
    val paths: MutableMap<PlayerId, Path>,
    val players: MutableMap<PlayerId, Player>,
    val nodesToEdges: MutableMap<Pair<NodeId, NodeId>, EdgeId>,
    var serverId: NodeId = NodeId(0),
    val mapSize: Coordinates,
) {
    fun addNode(node: Node) {
        nodes[node.id] = node
    }

    fun addEdge(edge: Edge) {
        edges[edge.id] = edge
        nodesToEdges[Pair(edge.firstNodeId, edge.secondNodeId)] = edge.id
        nodesToEdges[Pair(edge.secondNodeId, edge.firstNodeId)] = edge.id
    }

    fun addPlayer(player: Player) {
        player.id?.let { players[it] = player }
    }

    fun getEdgeId(firstId: NodeId, secondId: NodeId) : EdgeId? {
        val key: Pair<NodeId, NodeId> = firstId to secondId
        return nodesToEdges[key]
    }

}