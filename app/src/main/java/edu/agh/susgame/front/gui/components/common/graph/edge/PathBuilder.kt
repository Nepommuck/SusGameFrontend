package edu.agh.susgame.front.gui.components.common.graph.edge

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import edu.agh.susgame.front.gui.components.common.graph.node.NodeId

class PathBuilder(private val serverId: NodeId) {
    val path: MutableList<NodeId> = mutableListOf()
    val isPathValid: MutableState<Boolean> = mutableStateOf(false)

    fun addNode(nodeId: NodeId) {
        if (isNodeValid(nodeId)) {
            path.add(nodeId)
            updateValidity()
        }
    }

    fun getCurrentNumberOfNodes(): Int = path.size

    fun getLastNode(): NodeId? = path.lastOrNull()

    fun deleteNodeFromPath(nodeId: NodeId) {
        val nodeIndex = path.indexOf(nodeId)
        if (nodeIndex != -1) {
            path.removeAll(
                elements = path.filterIndexed { index, _ -> index > nodeIndex },
            )
        }
        updateValidity()
    }

    fun isNodeValid(nodeId: NodeId): Boolean {
        return !path.contains(nodeId)
    }

    private fun updateValidity() {
        this.isPathValid.value = path.lastOrNull() == serverId
    }

    override fun toString(): String = path.joinToString(", ") { it.value.toString() }

    fun reset() {
        this.path.clear()
        this.isPathValid.value = false
    }
}
