package edu.agh.susgame.front.managers

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
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
import edu.agh.susgame.front.managers.state.GameStateManager
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.GameService.SimpleMessage

class GameManager(
    val nodesList: List<Node>,
    val edgesList: List<Edge>,
    val playersList: List<PlayerLobby>,
    val serverId: NodeId,
    val mapSize: Coordinates,
    val criticalBufferOverheatLevel: Int,
    val packetsToWin: Int,
    val localPlayerId: PlayerId,
) {
    // Shared game state
    val gameState = GameStateManager()

    // ATTRIBUTES - DEFAULT
    private var gameService: GameService? = null
    private val pathsByPlayerId: SnapshotStateMap<PlayerId, Path> = mutableStateMapOf()
    val isPathBeingChanged: MutableState<Boolean> = mutableStateOf(false)
    val gameStatus: MutableState<GameStatus> = mutableStateOf(GameStatus.WAITING)
    val gameTimeLeft: MutableIntState = mutableIntStateOf(0)
    val chatMessages: SnapshotStateList<SimpleMessage> = mutableStateListOf()
    val pathBuilder: PathBuilder = PathBuilder(serverId)

    // ATTRIBUTES - WITH INIT
    private val playerIdByHostId: Map<NodeId, PlayerId> = initPlayerIdByHostId()
    private val edgesById: Map<EdgeId, Edge> = initEdgesById()
    private val nodesIdsToEdgeId: Map<Pair<NodeId, NodeId>, EdgeId> = initNodesIdsToEdgeId()
    private val server: Server = initServer()
    val hostIdByPlayerId: Map<PlayerId, NodeId> = initHostIdByPlayerId()
    val nodesById: Map<NodeId, Node> = initNodesById()
    val playersById: Map<PlayerId, PlayerLobby> = initPlayersById()


    init {
        initHostMaxPackets()
    }

    // SERVICE MANAGEMENT - UPDATE FROM SERVER MESSAGES
    fun updateChat(author: PlayerNickname, message: String) {
        chatMessages.add(SimpleMessage(author, message))
    }

    fun updateHostFlow(hostId: NodeId, flow: Int) {
        (nodesById[hostId] as? Host?)?.packetsToSend?.intValue = flow
    }

    fun updateServerReceivedPackets(packets: Int) {
        server.packetsReceived.intValue = packets
    }

    fun updatePlayerTokens(playerId: PlayerId, tokens: Int) {
        playersById[playerId]?.tokens?.intValue = tokens
    }

    fun updateRouterBufferSize(nodeId: NodeId, size: Int) {
        (nodesById[nodeId] as? Router?)?.bufferSize?.intValue = size
    }

    fun updateRouterCurrentPackets(nodeId: NodeId, packets: Int) {
        (nodesById[nodeId] as? Router?)?.bufferCurrentPackets?.intValue = packets
    }

    fun updateRouterUpgradeCost(nodeId: NodeId, cost: Int) {
        (nodesById[nodeId] as? Router?)?.upgradeCost?.intValue = cost
    }

    fun updateRouterState(nodeId: NodeId, isWorking: Boolean) {
        (nodesById[nodeId] as? Router?)?.isWorking?.value = isWorking
    }

    fun updateRouterOverheat(nodeId: NodeId, overheat: Int) {
        (nodesById[nodeId] as? Router?)?.overheat?.intValue = overheat
    }

    fun updatePath(hostId: NodeId, route: List<Int>) {
        if (playerIdByHostId[hostId] != localPlayerId) {
            clearEdges(playerIdByHostId[hostId])
            val path = listOf(hostId.value) + route
            path.zipWithNext { host1, host2 ->
                updateEdge(
                    NodeId(host1),
                    NodeId(host2),
                    playerIdByHostId[hostId]
                )
            }
        }
    }

    fun updateGameTime(time: Int) {
        gameTimeLeft.intValue = time
    }

    fun updateGameStatus(status: GameStatus) {
        gameStatus.value = status
    }

    // HANDLE GUI INPUT
    fun handleHostFlowChange(hostId: NodeId, flow: Int) {
        (nodesById[hostId] as? Host)?.let { host ->
            host.packetsToSend.intValue = flow
            gameService?.sendHostFlow(hostId, flow)
        }
    }

    fun handleRouterRepair(nodeId: NodeId) {
        (nodesById[nodeId] as? Router?)?.isWorking?.value = false
        gameService?.sendFixRouter(nodeId)
    }

    fun handleRouterUpgrade(nodeId: NodeId) {
        gameService?.sendUpgradeRouter(nodeId)
    }

    fun handlePathChange() {
        val path = Path(pathBuilder.path)
        pathsByPlayerId[localPlayerId] = path
        gameService?.sendHostRouteUpdate(
            path.path.first(), path.path.drop(1)
        )
        pathBuilder.reset()
    }

    fun handleSendingMessage(text: String) {
        val message = SimpleMessage(
            author = playersById[localPlayerId]?.name ?: PlayerNickname("[???]"),
            message = text
        )
        chatMessages.add(message)
        gameService?.sendSimpleMessage(message.message)
    }

    // GUI GETTERS
    fun getServerReceivedPackets(): MutableIntState = server.packetsReceived

    fun getPlayerTokens(playerId: PlayerId): MutableIntState =
        playersById[playerId]?.tokens
            ?: throw IllegalStateException("Player ${playerId.value} doesn't exist in the map")

    // UTILS
    fun addNodeToPathBuilder(nodeId: NodeId) {
        if (pathBuilder.getCurrentNumberOfNodes() == 0) {
            pathBuilder.addNode(nodeId)
        } else {
            val edgeId = pathBuilder.getLastNode()?.let { lastNodeId ->
                getEdgeId(lastNodeId, nodeId)
            }
            edgeId?.let {
                if (pathBuilder.isNodeValid(nodeId)) {
                    pathBuilder.addNode(nodeId)
                    updateEdge(edgeId)
                }
            }
        }
    }

    fun clearEdges(playerId: PlayerId?) {
        if (playerId != null) {
            edgesList.forEach { edge ->
                edge.removePlayer(playerId)
            }
        }
    }

    fun addGameService(gameService: GameService) {
        this.gameService = gameService
    }

    // PRIVATE
    private fun getEdgeId(from: NodeId, to: NodeId): EdgeId? = nodesIdsToEdgeId[Pair(from, to)]
    private fun updateEdge(from: NodeId, to: NodeId, playerId: PlayerId?) {
        playerId?.let { edgesById[getEdgeId(from, to)]?.addPlayer(playerId) }
    }

    private fun updateEdge(edgeId: EdgeId) {
        edgesById[edgeId]?.addPlayer(localPlayerId)
    }

    // INIT
    private fun initHostIdByPlayerId(): Map<PlayerId, NodeId> {
        return nodesList.filterIsInstance<Host>()
            .associateBy { it.playerId }
            .mapValues { it.value.id }
    }

    private fun initPlayerIdByHostId(): Map<NodeId, PlayerId> {
        return initHostIdByPlayerId().entries.associate { (playerId, nodeId) -> nodeId to playerId }
    }

    private fun initNodesById(): Map<NodeId, Node> {
        return nodesList.associateBy { it.id }
    }

    private fun initEdgesById(): Map<EdgeId, Edge> {
        return edgesList.associateBy { it.id }
    }

    private fun initPlayersById(): Map<PlayerId, PlayerLobby> {
        return playersList.associateBy { it.id }
    }

    private fun initNodesIdsToEdgeId(): Map<Pair<NodeId, NodeId>, EdgeId> {
        return edgesList
            .associate { Pair(it.firstNodeId, it.secondNodeId) to it.id }
            .flatMap {
                sequenceOf(
                    it.toPair(),
                    Pair(it.key.second, it.key.first) to it.value
                )
            }.associate {
                it.first to it.second
            }
    }

    private fun initServer(): Server {
        return initNodesById()[serverId] as? Server
            ?: throw IllegalStateException("Server could not be initialized using id ${serverId.value}")
    }

    private fun initHostMaxPackets() {
        initHostIdByPlayerId().values.forEach { hostId ->
            val matchingEdges = edgesList.filter { edge ->
                edge.firstNodeId == hostId || edge.secondNodeId == hostId
            }
            if (matchingEdges.isNotEmpty()) {
                (initNodesById()[hostId] as? Host?)?.maxPacketsToSend?.intValue =
                    matchingEdges[0].bandwidth
            }
        }
    }
}
