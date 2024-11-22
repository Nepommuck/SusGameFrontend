package edu.agh.susgame.front.managers

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerREST
import edu.agh.susgame.front.gui.components.common.graph.edge.Edge
import edu.agh.susgame.front.gui.components.common.graph.edge.EdgeId
import edu.agh.susgame.front.gui.components.common.graph.edge.Path
import edu.agh.susgame.front.gui.components.common.graph.node.Host
import edu.agh.susgame.front.gui.components.common.graph.node.Node
import edu.agh.susgame.front.gui.components.common.graph.node.NodeId
import edu.agh.susgame.front.gui.components.common.util.Coordinates
import edu.agh.susgame.front.service.interfaces.GameService

class GameManager(
    nodesList: List<Node>,
    edgesList: List<Edge>,
    playersList: List<PlayerREST>,
    val serverId: NodeId,
    val mapSize: Coordinates,
    val packetsToWin: Int = 100
) {
    val hosts: Map<PlayerId, NodeId> = nodesList.filterIsInstance<Host>()
        .associateBy { it.playerId }
        .mapValues { it.value.id }

    val nodes: Map<NodeId, Node> = nodesList.associateBy { it.id }

    val edges: Map<EdgeId, Edge> = edgesList.associateBy { it.id }

    val players: Map<PlayerId, PlayerREST> = playersList.associateBy { it.id }

    val paths: MutableMap<PlayerId, Path> = mutableMapOf()

    val chatMessages: MutableSet<String> = mutableSetOf()

    val packetsReceived: MutableState<Int> = mutableIntStateOf(0)

    val playerMoney: MutableState<Int> = mutableIntStateOf(0)

    private val nodesToEdges = edgesList
        .associate { Pair(it.firstNodeId, it.secondNodeId) to it.id }
        .flatMap {
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

    fun getPlayerREST(playerId: PlayerId): PlayerREST? = players[playerId]

    fun getHostID(playerId: PlayerId): NodeId? = hosts[playerId]

    fun addMessage(message: GameService.SimpleMessage) {
        chatMessages.add("[${message.author.value}]: ${message.message}")
    }
}