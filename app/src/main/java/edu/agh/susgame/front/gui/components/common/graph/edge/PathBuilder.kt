package edu.agh.susgame.front.gui.components.common.graph.edge

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import edu.agh.susgame.front.gui.components.common.graph.node.NodeId

class PathBuilder(val serverId: NodeId) {
    val path: MutableList<NodeId> = mutableListOf()
    val isPathValid: MutableState<Boolean> = mutableStateOf(false)

    fun addNode(nodeId: NodeId) {
        if (isNodeValid(nodeId)) {
            path.add(nodeId)
            checkValidity()
        }
    }

    fun getLastNode(): NodeId? = path.lastOrNull()

    fun deleteNodeFromPath(nodeId: NodeId) {
        // TODO GAME-67
    }

    fun isNodeValid(nodeId: NodeId): Boolean {
        return !path.contains(nodeId)
    }

    fun checkValidity() {
        this.isPathValid.value = path.lastOrNull() == serverId
    }

    fun getPathString(): String {
        return path.joinToString(", ") { it.value.toString() }
    }

    fun reset() {
        this.path.clear()
        this.isPathValid.value=false
    }
}