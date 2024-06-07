package edu.agh.susgame.front.model.graph

class Path(
    val path: MutableList<NodeId> = mutableListOf()
) {
    fun addNodeToPath(nodeId: NodeId) {
        if (isNodeCorrect(nodeId)) path.add(nodeId);

    }
    fun deleteNodeFromPath(nodeId: NodeId){

    }
    fun isNodeCorrect(nodeId: NodeId): Boolean {
        return !path.contains(nodeId)
    }
    fun getPathString(): String {
        return path.joinToString(", ") { it.value.toString() }
    }
}