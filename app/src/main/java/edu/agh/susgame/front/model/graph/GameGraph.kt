package edu.agh.susgame.front.model.graph

import edu.agh.susgame.dto.rest.model.Player
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.front.util.Coordinates

class GameGraph(
    val nodes: Map<NodeId, Node>,
    val edges: Map<EdgeId, Edge>,
    val players: Map<PlayerId, Player>,
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
            players: List<Player>,
            serverId: NodeId,
            mapSize: Coordinates,
        ): GameGraph =
            GameGraph(
                nodes = nodes.associateBy { it.id },
                edges = edges.associateBy { it.id },
                players = players.associateBy { it.id },
                paths = mutableMapOf(),
                serverId = serverId,
                mapSize = mapSize,
            )
    }
}