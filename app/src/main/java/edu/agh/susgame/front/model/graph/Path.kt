package edu.agh.susgame.front.model.graph

class PathBuilder {
    val path: MutableList<NodeId> = mutableListOf()
    fun addNodeToPath(nodeId: NodeId) {
        if (isNodeValid(nodeId)) {
            path.add(nodeId)
        }
    }

    fun deleteNodeFromPath(nodeId: NodeId) {
        // TODO GAME-67
    }

    fun isNodeValid(nodeId: NodeId): Boolean {
        return !path.contains(nodeId)
    }

    fun isPathValid(serverId: NodeId): Boolean {
        return path.lastOrNull() == serverId
    }

    fun getPathString(): String {
        return path.joinToString(", ") { it.value.toString() }
    }
}

class Path(
    private val path: List<NodeId>
) {
    fun getPathString(): String {
        return path.joinToString(", ") { it.value.toString() }
    }
}