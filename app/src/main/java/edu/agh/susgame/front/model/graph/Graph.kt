package edu.agh.susgame.front.model.graph

import edu.agh.susgame.front.util.Coordinates

class Graph(
    val nodeMap: MutableMap<Int, Node>,
    val edgeMap: MutableMap<Int, Edge>,
    val mapSize: Coordinates,
) {
    fun addNode(node: Node) {
        nodeMap[node.id] = node
    }

    fun addEdge(edge: Edge) {
        edgeMap[edge.id] = edge
    }
}