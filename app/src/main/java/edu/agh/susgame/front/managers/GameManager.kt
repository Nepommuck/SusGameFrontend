package edu.agh.susgame.front.managers

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.socket.ServerSocketMessage
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

class GameManager(
    val nodesList: List<Node>,
    val edgesList: List<Edge>,
    val playersList: List<PlayerLobby>,
    val serverId: NodeId,
    val mapSize: Coordinates,
    val packetsToWin: Int = 400,
    val localPlayerId: PlayerId,
    private var gameService: GameService? = null,
    val isPathBeingChanged: MutableState<Boolean> = mutableStateOf(false)
) {
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
    fun setServerReceivedPackets(packets: Int) {
        (nodesById[serverId] as? Server)?.packetsReceived?.intValue = packets
    }

    fun getServerReceivedPackets(): MutableIntState =
        (nodesById[serverId] as? Server)?.packetsReceived ?: mutableIntStateOf(2137)

    fun setPlayerTokens(playerId: PlayerId, tokens: Int) {
        playersById[playerId]?.tokens?.intValue = tokens
    }

    fun getPlayerTokens(playerId: PlayerId): MutableIntState =
        playersById[playerId]?.tokens ?: mutableIntStateOf(2137)

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

    fun setHostFlow(hostId: NodeId, flow: Int) {
        if (nodesById[hostId] is Host) {
            (nodesById[hostId] as Host).packetsToSend.value = flow
            gameService?.sendHostFlow(hostId, flow)
        }
    }

    fun repairRouter(nodeId: NodeId) {
        print("repairing router!")
        (nodesById[nodeId] as? Router?)?.isOverloaded?.value = false
    }

    fun upgradeRouter(nodeId: NodeId) {
        print("upgrading router!")
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

    fun addMessage(message: SimpleMessage) {
        chatMessages.add(message)
    }

    fun updatePathFromLocal(path: Path) {
        println("Updating path from local$path")
        pathsByPlayerId[localPlayerId] = path
        gameService?.sendHostRouteUpdate(
            path.path[0], path.path.drop(1)
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
