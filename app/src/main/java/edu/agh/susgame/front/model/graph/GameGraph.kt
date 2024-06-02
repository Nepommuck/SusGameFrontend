package edu.agh.susgame.front.model.graph

import edu.agh.susgame.front.util.Coordinates

class GameGraph(
    val nodes: MutableMap<Int, Node>,
    val edges: MutableMap<Int, Edge>,
    val mapSize: Coordinates,
) {
    fun addNode(node: Node) {
        nodes[node.id] = node
    }

    fun addEdge(edge: Edge) {
        edges[edge.id] = edge
    }
}