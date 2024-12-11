package edu.agh.susgame.front.gui.components.common.graph.node

import edu.agh.susgame.front.gui.components.common.util.Coordinates


data class NodeId(val value: Int)

sealed class Node(
    val id: NodeId,
    val name: String,
    val position: Coordinates,
) {
    abstract fun getNodeName(): String
}
