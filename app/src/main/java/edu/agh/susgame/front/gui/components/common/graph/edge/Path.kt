package edu.agh.susgame.front.gui.components.common.graph.edge

import edu.agh.susgame.front.gui.components.common.graph.node.NodeId

class Path(
    val path: List<NodeId>
) {
    override fun toString() = path.joinToString(", ") { it.value.toString() }
}
