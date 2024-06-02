package edu.agh.susgame.front.model.graph

class Edge(
    val id: Int,
    val firstNodeId: Int,
    val secondNodeId: Int,
    var bandwidth: Int,
)