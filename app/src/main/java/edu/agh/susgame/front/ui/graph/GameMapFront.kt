package edu.agh.susgame.front.ui.graph

import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerREST
import edu.agh.susgame.front.ui.graph.node.Node
import edu.agh.susgame.front.ui.graph.node.NodeId
import edu.agh.susgame.front.ui.components.common.util.Coordinates

class GameMapFront(
    val nodes: Map<NodeId, Node>,
    val edges: Map<EdgeId, Edge>,
    val players: Map<PlayerId, PlayerREST>,
    val paths: MutableMap<PlayerId, Path>,
    val serverId: NodeId,
    val mapSize: Coordinates,
) {
    private val nodesToEdges = edges
        .values
        .associate {
            Pair(it.firstNodeId, it.secondNodeId) to it.id
        }.flatMap {
            sequenceOf(
                it.toPair(),
                Pair(it.key.second, it.key.first) to it.value
            )
        }.associate {
            it.first to it.second
        }

    fun getEdgeId(firstNode: NodeId, secondNode: NodeId): EdgeId? {
        return this.nodesToEdges[Pair(firstNode, secondNode)]
    }

    companion object {
        fun fromLists(
            nodes: List<Node>,
            edges: List<Edge>,
            players: List<PlayerREST>,
            serverId: NodeId,
            mapSize: Coordinates,
        ): GameMapFront =
            GameMapFront(
                nodes = nodes.associateBy { it.id },
                edges = edges.associateBy { it.id },
                players = players.associateBy { it.id },
                paths = mutableMapOf(),
                serverId = serverId,
                mapSize = mapSize,
            )
    }
}