package edu.agh.susgame.front.managers

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerREST
import edu.agh.susgame.dto.socket.ServerSocketMessage
import edu.agh.susgame.front.gui.components.common.graph.edge.Edge
import edu.agh.susgame.front.gui.components.common.graph.edge.EdgeId
import edu.agh.susgame.front.gui.components.common.graph.edge.Path
import edu.agh.susgame.front.gui.components.common.graph.edge.PathBuilder
import edu.agh.susgame.front.gui.components.common.graph.node.Host
import edu.agh.susgame.front.gui.components.common.graph.node.Node
import edu.agh.susgame.front.gui.components.common.graph.node.NodeId
import edu.agh.susgame.front.gui.components.common.util.Coordinates
import edu.agh.susgame.front.service.interfaces.GameService

class GameManager(
    val nodesList: List<Node>,
    val edgesList: List<Edge>,
    val playersList: List<PlayerREST>,
    val serverId: NodeId,
    val mapSize: Coordinates,
    val packetsToWin: Int = 100,
    val localPlayerId: PlayerId,
    private var gameService: GameService? = null,
    val isPathBeingChanged: MutableState<Boolean> = mutableStateOf(false)
) {
    // ADDERS
    fun addGameService(gameService: GameService) {
        this.gameService = gameService
    }

    // MAPS
    private val hostIdByPlayerId: Map<PlayerId, NodeId> = nodesList.filterIsInstance<Host>()
        .associateBy { it.playerId }
        .mapValues { it.value.id }

    private val playerIdByHostId: Map<NodeId, PlayerId> =
        hostIdByPlayerId.entries.associate { (playerId, nodeId) -> nodeId to playerId }

    val nodesById: Map<NodeId, Node> = nodesList.associateBy { it.id }

    val edgesById: Map<EdgeId, Edge> = edgesList.associateBy { it.id }

    val playersById: Map<PlayerId, PlayerREST> = playersList.associateBy { it.id }

    private val nodesIdsToEdgeId = edgesList
        .associate { Pair(it.firstNodeId, it.secondNodeId) to it.id }
        .flatMap {
            sequenceOf(
                it.toPair(),
                Pair(it.key.second, it.key.first) to it.value
            )
        }.associate {
            it.first to it.second
        }

    // MUTABLE
    val pathsByPlayerId: SnapshotStateMap<PlayerId, Path> = mutableStateMapOf()

    // TODO GAME-54 Undo this
    val chatMessages: MutableSet<String> = mutableSetOf(
        "Hello there",
        "You all",
        "You all",
        "You all",
        "You all",
        "You all",
        "You all",
        "You all",
        "You all",
        "You all",
        "You all",
        "You all",
        "You all",
        "You all",
        "You all",
        "You all",
        "You all",
    )
//    val chatMessages: MutableSet<String> = mutableSetOf()

    val packetsReceived: MutableState<Int> = mutableIntStateOf(0)

    val playerMoney: MutableState<Int> = mutableIntStateOf(0)

    var pathBuilder: MutableState<PathBuilder> = mutableStateOf(PathBuilder(serverId))

    // PRIVATE METHODS
    private fun getEdgeId(from: NodeId, to: NodeId): EdgeId? {
        return this.nodesIdsToEdgeId[Pair(from, to)]
    }

    private fun updateEdge(from: NodeId, to: NodeId, playerId: PlayerId?) {
        println("Updating edge$playerId")
        playerId?.let { edgesById[getEdgeId(from, to)]?.addPlayer(playerId) }
    }

    private fun updateEdge(edgeId: EdgeId) {
        println("Updating edge from id$edgeId")
        edgesById[edgeId]?.addPlayer(localPlayerId)
    }

    // PUBLIC METHODS
    fun addFirstNodeToPathBuilder(nodeId: NodeId) {
        pathBuilder.value.addNode(nodeId)
    }

    fun addNodeToPathBuilder(nodeId: NodeId) {
        println("addNodeToPathBuilder")
        val edgeId = pathBuilder.value.getLastNode()?.let { lastNodeId ->
            getEdgeId(lastNodeId, nodeId)
        }
        edgeId?.let {
            if (pathBuilder.value.isNodeValid(nodeId)) {
                pathBuilder.value.addNode(nodeId)
                updateEdge(edgeId)
            }
        }
    }

    fun clearEdgesLocal() {
        println("ClearEdges")
        edgesList.forEach { edge ->
            edge.removePlayer(localPlayerId)
        }
    }

    fun addMessage(message: GameService.SimpleMessage) {
        chatMessages.add("[${message.author.value}]: ${message.message}")
    }

    fun updatePathFromLocal(path: Path) {
        println("Updating path from local$path")
        pathsByPlayerId[localPlayerId] = path
        gameService?.sendHostUpdate(
            path.path[0], path.path.drop(1), 2
        )
    }

    fun updatePathsFromServer(decodedMessage: ServerSocketMessage.GameState) {
        decodedMessage.hosts.forEach { host ->
            val path = listOf(host.id) + host.packetRoute
            for (i in 0 until path.size - 1) {
                if (playerIdByHostId[NodeId(host.id)] != localPlayerId) {
                    updateEdge(
                        NodeId(path[i]),
                        NodeId(path[i + 1]),
                        playerIdByHostId[NodeId(host.id)]
                    )
                }
            }
        }
    }
}
