package edu.agh.susgame.front.ui.graph

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerREST
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.ui.components.common.util.Coordinates
import edu.agh.susgame.front.ui.graph.node.Host
import edu.agh.susgame.front.ui.graph.node.Node
import edu.agh.susgame.front.ui.graph.node.NodeId

class GameManager(
    val hosts: Map<PlayerId, NodeId>,
    val nodes: Map<NodeId, Node>,
    val edges: Map<EdgeId, Edge>,
    val players: Map<PlayerId, PlayerREST>,
    val paths: MutableMap<PlayerId, Path>,
    val serverId: NodeId,
    val mapSize: Coordinates,
    val chatMessages: MutableSet<String> = mutableSetOf(),
    val packetsRec: MutableState<Int> = mutableIntStateOf(0),
    val playerMoney: MutableState<Int> = mutableIntStateOf(0),
    val packetsToWin: Int = 100
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

    fun getPlayerREST(playerId: PlayerId): PlayerREST? = players[playerId]

    fun getHostID(playerId: PlayerId): NodeId? = hosts[playerId]

    fun addMessage(message: GameService.SimpleMessage) {
        chatMessages.add(("[${message.author.value}]: ${message.message}"))
    }

    fun testEdge() {
        edges.forEach { (edgeid, edge) -> edge.addPlayer(PlayerId(0)) }
    }

//
//    fun upgradePacketsReceived(packets: Int){
//        packetsRec = packets
//        println("upgrade")
//    }

//    fun getPacketsReceived(): MutableState<Int> {
//        return packetsRec
//    }

    companion object {
        fun fromLists(
            nodes: List<Node>,
            edges: List<Edge>,
            players: List<PlayerREST>,
            serverId: NodeId,
            mapSize: Coordinates,
        ): GameManager =
            GameManager(
                hosts = nodes.filterIsInstance<Host>()
                    .associateBy { it.playerId }
                    .mapValues { it.value.id },
                nodes = nodes.associateBy { it.id },
                edges = edges.associateBy { it.id },
                players = players.associateBy { it.id },
                paths = mutableMapOf(),
                serverId = serverId,
                mapSize = mapSize,

            )
    }
}