package edu.agh.susgame.front.gui.components.common.graph.node

import edu.agh.susgame.front.gui.components.common.util.Coordinates


data class NodeId(val value: Int)

sealed class Node(
    val id: NodeId,
    val position: Coordinates,
) {
    abstract val name: String

    abstract fun getInfo(): String
}
