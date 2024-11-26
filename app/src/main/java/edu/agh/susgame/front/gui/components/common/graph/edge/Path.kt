package edu.agh.susgame.front.gui.components.common.graph.edge

import edu.agh.susgame.front.gui.components.common.graph.node.NodeId

class Path(
    val path: List<NodeId>
) {
    fun getPathString(): String {
        return path.joinToString(", ") { it.value.toString() }
    }
}