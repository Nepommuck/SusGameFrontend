package edu.agh.susgame.front.model.graph

import androidx.compose.ui.graphics.Color

data class EdgeId(val value: Int)

class Edge(
    val id: EdgeId,
    val firstNodeId: NodeId,
    val secondNodeId: NodeId,
    var bandwidth: Int,
    var color: Color = Color.Red,
)