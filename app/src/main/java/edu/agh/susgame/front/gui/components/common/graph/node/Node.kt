package edu.agh.susgame.front.gui.components.common.graph.node

import edu.agh.susgame.front.gui.components.common.util.Coordinates


data class NodeId(val value: Int)

sealed class Node(
    val id: NodeId,
    val name: String,
    val position: Coordinates,
) {
    // TODO fow now it's in string, later on this should be improved to something more accurate
    abstract fun getInfo(): String
}
