package edu.agh.susgame.front.managers

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
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
    val playersById: Map<PlayerId, PlayerLobby>,
    val serverId: NodeId,
    val mapSize: Coordinates,
    val criticalBufferOverheatLevel: Int,
    val packetsToWin: Int,
    val localPlayerId: PlayerId,
    private val gameService: GameService,
) {
    // Shared game state
    val gameState = GameStateManager(mapSize)

    val quizManager = QuizManager(gameService)

    // ATTRIBUTES - DEFAULT
    private val pathsByPlayerId: SnapshotStateMap<PlayerId, Path> = mutableStateMapOf()
    val gameTimeLeft: MutableIntState = mutableIntStateOf(0)
    val chatMessages: SnapshotStateList<SimpleMessage> = mutableStateListOf()
    val pathBuilder: PathBuilder = PathBuilder(serverId)

    // ATTRIBUTES - WITH INIT
    private val playerIdByHostId: Map<NodeId, PlayerId> = initPlayerIdByHostId()
    private val edgesById: SnapshotStateMap<EdgeId, Edge> = initEdgesById()
    private val nodesIdsToEdgeId: Map<Pair<NodeId, NodeId>, EdgeId> = initNodesIdsToEdgeId()
    private val server: Server = initServer()
    val hostIdByPlayerId: Map<PlayerId, NodeId> = initHostIdByPlayerId()
    val nodesById: Map<NodeId, Node> = initNodesById()


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
        clearRouters(playerIdByHostId[hostId])
        route.dropLast(1).forEach {
            playerIdByHostId[hostId]?.let { playerId ->
                (nodesById[NodeId(it)] as? Router?)?.playersSet?.add(
                    playerId
                )
            }
        }
        if (playerIdByHostId[hostId] != localPlayerId) {
            clearEdges(playerIdByHostId[hostId])
            val path = listOf(hostId.value) + route

            path.zipWithNext { host1, host2 ->
                updateEdge(
                    NodeId(host1), NodeId(host2), playerIdByHostId[hostId]
                )
            }
        }
    }

    fun updateGameTime(time: Int) {
        gameTimeLeft.intValue = time
    }

    fun updateGameStatus(status: GameStatus) {
        gameState.gameStatus.value = status
    }

    fun updateEdge(edgeId: EdgeId, upgradeCost: Int, packetsTransported: Int) {
        edgesById[edgeId]?.packetsTransported?.intValue = packetsTransported
        edgesById[edgeId]?.upgradeCost?.intValue = upgradeCost
    }

    // HANDLE GUI INPUT
    fun handleHostFlowChange(hostId: NodeId, flow: Int) {
        (nodesById[hostId] as? Host)?.let { host ->
            host.packetsToSend.intValue = flow
            gameService.sendHostFlow(hostId, flow)
        }
    }

    fun handleRouterRepair(nodeId: NodeId) {
        (nodesById[nodeId] as? Router?)?.isWorking?.value = false
        gameService.sendFixRouter(nodeId)
    }

    fun handleRouterUpgrade(nodeId: NodeId) {
        gameService.sendUpgradeRouter(nodeId)
    }

    fun handlePathChange() {
        val path = Path(pathBuilder.path.toList())
        pathsByPlayerId[localPlayerId] = path
        gameService.sendHostRouteUpdate(
            hostId = path.path.first(),
            packetPath = path.path.drop(1),
        )
        pathBuilder.reset()
    }

    fun handleSendingMessage(text: String) {
        if (text.isNotEmpty()) {
            val message = SimpleMessage(
                author = playersById[localPlayerId]?.name ?: PlayerNickname("[???]"), message = text
            )
            chatMessages.add(message)
            gameService.sendSimpleMessage(message.message)
        }
    }

    // GUI GETTERS
    fun getServerReceivedPackets(): MutableIntState = server.packetsReceived

    fun getPlayerTokens(playerId: PlayerId): MutableIntState = playersById[playerId]?.tokens
        ?: throw IllegalStateException("Player ${playerId.value} doesn't exist in the map")

    fun getPlayerColor(playerId: PlayerId): Color = playersById[playerId]?.color?.value
        ?: throw IllegalStateException("Player ${playerId.value} doesn't exist in the map")

    // UTILS
    fun addNodeToPathBuilder(nodeId: NodeId) {
        if (pathBuilder.getCurrentNumberOfNodes() == 0) {
            pathBuilder.addNode(nodeId)
        } else if (nodeId !in pathBuilder.path) {
            val edgeId = pathBuilder.getLastNode()?.let { lastNodeId ->
                getEdgeId(lastNodeId, nodeId)
            }
            edgeId?.let {
                if (pathBuilder.isNodeValid(nodeId)) {
                    pathBuilder.addNode(nodeId)
                    addPlayerToEdge(edgeId)
                }
            }
        } else {
            pathBuilder.deleteNodeFromPath(nodeId)
            clearEdges(localPlayerId)
            pathBuilder.path.zipWithNext { host1, host2 ->
                updateEdge(
                    NodeId(host1.value), NodeId(host2.value), localPlayerId
                )
            }
        }
    }

    fun clearEdges(playerId: PlayerId?) {
        playerId?.let {
            edgesList.forEach { edge ->
                edge.removePlayer(it)
            }
        }
    }

    fun cancelChangingPath(){
        clearEdges(localPlayerId)
        gameState.isPathBeingChanged.value = false
        pathBuilder.reset()
        pathsByPlayerId[localPlayerId]?.path?.zipWithNext { node1, node2 ->
            updateEdge(
                node1, node2, localPlayerId
            )
        }
    }

    private fun clearRouters(playerId: PlayerId?) {
        playerId?.let {
            nodesList.filterIsInstance<Router>()
                .forEach { router -> router.playersSet.remove(it) }
        }
    }

    // PRIVATE
    private fun getEdgeId(from: NodeId, to: NodeId): EdgeId? = nodesIdsToEdgeId[Pair(from, to)]
    private fun updateEdge(from: NodeId, to: NodeId, playerId: PlayerId?) {
        playerId?.let { edgesById[getEdgeId(from, to)]?.addPlayer(playerId) }
    }

    private fun addPlayerToEdge(edgeId: EdgeId) {
        edgesById[edgeId]?.addPlayer(localPlayerId)
    }

    // INIT
    private fun initHostIdByPlayerId(): Map<PlayerId, NodeId> {
        return nodesList.filterIsInstance<Host>().associateBy { it.playerId }
            .mapValues { it.value.id }
    }

    private fun initPlayerIdByHostId(): Map<NodeId, PlayerId> {
        return initHostIdByPlayerId().entries.associate { (playerId, nodeId) -> nodeId to playerId }
    }

    private fun initNodesById(): Map<NodeId, Node> {
        return nodesList.associateBy { it.id }
    }

    private fun initEdgesById(): SnapshotStateMap<EdgeId, Edge> {
        return mutableStateMapOf(*edgesList.associateBy { it.id }.toList().toTypedArray())
    }

    private fun initNodesIdsToEdgeId(): Map<Pair<NodeId, NodeId>, EdgeId> {
        return edgesList.associate { Pair(it.firstNodeId, it.secondNodeId) to it.id }.flatMap {
            sequenceOf(
                it.toPair(), Pair(it.key.second, it.key.first) to it.value
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
            val neighbouringHostEdge = edgesList.first { edge ->
                edge.firstNodeId == hostId || edge.secondNodeId == hostId
            }

            val routerId = if (neighbouringHostEdge.firstNodeId != hostId) {
                neighbouringHostEdge.firstNodeId
            } else {
                neighbouringHostEdge.secondNodeId
            }

            val router = initNodesById()[routerId] as? Router

            router?.let {
                (initNodesById()[hostId] as? Host)?.maxPacketsToSend = it.bufferSize
            }
        }
    }

}
