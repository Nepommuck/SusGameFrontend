package edu.agh.susgame.front.managers

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.socket.ServerSocketMessage
import edu.agh.susgame.dto.socket.common.GameStatus
import edu.agh.susgame.front.gui.components.common.graph.edge.Edge
import edu.agh.susgame.front.gui.components.common.graph.edge.EdgeId
import edu.agh.susgame.front.gui.components.common.graph.edge.Path
import edu.agh.susgame.front.gui.components.common.graph.edge.PathBuilder
import edu.agh.susgame.front.gui.components.common.graph.node.Host
import edu.agh.susgame.front.gui.components.common.graph.node.Node
import edu.agh.susgame.front.gui.components.common.graph.node.NodeId
import edu.agh.susgame.front.gui.components.common.graph.node.Router
import edu.agh.susgame.front.gui.components.common.graph.node.Server
import edu.agh.susgame.front.gui.components.common.util.Coordinates
import edu.agh.susgame.front.gui.components.common.util.player.PlayerLobby
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.GameService.SimpleMessage
import java.util.Locale

class GameManager(
    val nodesList: List<Node>,
    val edgesList: List<Edge>,
    val playersList: List<PlayerLobby>,
    val serverId: NodeId,
    val mapSize: Coordinates,
    val packetsToWin: Int = 400, // TODO get this from server
    val localPlayerId: PlayerId,
    private var gameService: GameService? = null,
) {
    val isPathBeingChanged: MutableState<Boolean> = mutableStateOf(false)
    val gameStatus: MutableState<GameStatus> = mutableStateOf(GameStatus.WAITING)
    val gameTimeLeft: MutableIntState = mutableIntStateOf(0)
    // ADDERS
    fun addGameService(gameService: GameService) {
        this.gameService = gameService
    }

    // MAPS
    val hostIdByPlayerId: Map<PlayerId, NodeId> = nodesList.filterIsInstance<Host>()
        .associateBy { it.playerId }
        .mapValues { it.value.id }

    private val playerIdByHostId: Map<NodeId, PlayerId> =
        hostIdByPlayerId.entries.associate { (playerId, nodeId) -> nodeId to playerId }

    val nodesById: Map<NodeId, Node> = nodesList.associateBy { it.id }

    val edgesById: Map<EdgeId, Edge> = edgesList.associateBy { it.id }

    val playersById: Map<PlayerId, PlayerLobby> = playersList.associateBy { it.id }

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
    private val server: Server = nodesById[serverId] as? Server?
        ?: throw IllegalStateException("Server could not be initialized using id ${serverId.value}")

    init {
        hostIdByPlayerId.values.forEach { hostId ->
            val matchingEdges = edgesList.filter { edge ->
                edge.firstNodeId == hostId || edge.secondNodeId == hostId
            }
            if (matchingEdges.isNotEmpty()) {
                (nodesById[hostId] as? Host?)?.maxPacketsToSend?.value = matchingEdges[0].bandwidth
            }
        }
    }

    // MUTABLE
    val pathsByPlayerId: SnapshotStateMap<PlayerId, Path> = mutableStateMapOf()

    val chatMessages = mutableStateListOf<SimpleMessage>()

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
    fun getTime(): State<String> {
        return derivedStateOf {
            val minutes = this.gameTimeLeft.intValue / 60
            val remainingSeconds = this.gameTimeLeft.intValue % 60
            String.format(Locale.US, "%02d:%02d", minutes, remainingSeconds)
        }
    }

    fun setServerReceivedPackets(packets: Int) {
        server.packetsReceived.intValue = packets
    }

    fun getServerReceivedPackets(): MutableIntState =
        server.packetsReceived

    fun setPlayerTokens(playerId: PlayerId, tokens: Int) {
        playersById[playerId]?.tokens?.intValue = tokens
    }

    fun getPlayerTokens(playerId: PlayerId): MutableIntState =
        playersById[playerId]?.tokens
            ?: throw IllegalStateException("Player ${playerId.value} doesn't exist in the map")

    fun setRouterBufferSize(nodeId: NodeId, size: Int) {
        (nodesById[nodeId] as? Router?)?.bufferSize?.value = size
    }

    fun setRouterCurrentPackets(nodeId: NodeId, packets: Int) {
        (nodesById[nodeId] as? Router?)?.bufferCurrentPackets?.value = packets
    }

    fun setRouterUpgradeCost(nodeId: NodeId, cost: Int) {
        (nodesById[nodeId] as? Router?)?.upgradeCost?.value = cost
    }

    fun addFirstNodeToPathBuilder(nodeId: NodeId) {
        pathBuilder.value.addNode(nodeId)
    }

    fun setHostFlow(hostId: NodeId, flow: Int) = (nodesById[hostId] as? Host)
        ?.let { host ->
            host.packetsToSend.value = flow
            gameService?.sendHostFlow(hostId, flow)
        }

    fun repairRouter(nodeId: NodeId) {
        (nodesById[nodeId] as? Router?)?.isOverloaded?.value = false
    }

    fun upgradeRouter(nodeId: NodeId) {
        gameService?.sendUpgradeRouter(nodeId)
    }

    fun addNodeToPathBuilder(nodeId: NodeId) {
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

    fun clearEdgesLocal(playerId: PlayerId?) {
        println("ClearEdges")
        if (playerId != null) {
            edgesList.forEach { edge ->
                edge.removePlayer(playerId)
            }
        }
    }

    fun addMessage(message: SimpleMessage) {
        chatMessages.add(message)
    }

    fun updatePathFromLocal(path: Path) {
        println("Updating path from local$path")
        pathsByPlayerId[localPlayerId] = path
        gameService?.sendHostRouteUpdate(
            path.path.first(), path.path.drop(1)
        )
    }

    fun updatePathsFromServer(decodedMessage: ServerSocketMessage.GameState) {
        decodedMessage.hosts.forEach { host ->
            if (playerIdByHostId[NodeId(host.id)] != localPlayerId) {
                clearEdgesLocal(playerIdByHostId[NodeId(host.id)])
                val path = listOf(host.id) + host.packetRoute
                path.zipWithNext { host1, host2 ->
                    updateEdge(
                        NodeId(host1),
                        NodeId(host2),
                        playerIdByHostId[NodeId(host.id)]
                    )
                }
            }
        }
    }
}
