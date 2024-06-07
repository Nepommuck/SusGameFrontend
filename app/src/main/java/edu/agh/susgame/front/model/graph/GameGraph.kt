package edu.agh.susgame.front.model.graph

import edu.agh.susgame.front.model.PlayerId
import edu.agh.susgame.front.util.Coordinates

class GameGraph(
    val nodes: MutableMap<NodeId, Node>,
    val edges: MutableMap<EdgeId, Edge>,
    val paths: MutableMap<PlayerId, Path>,
    val nodesToEdges: MutableMap<Pair<NodeId, NodeId>, EdgeId>,
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