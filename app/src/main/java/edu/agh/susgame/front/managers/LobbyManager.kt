package edu.agh.susgame.front.managers

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import edu.agh.susgame.dto.rest.model.GameMapDTO
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerREST
import edu.agh.susgame.front.gui.components.common.graph.edge.Edge
import edu.agh.susgame.front.gui.components.common.graph.edge.EdgeId
import edu.agh.susgame.front.gui.components.common.graph.node.Host
import edu.agh.susgame.front.gui.components.common.graph.node.Node
import edu.agh.susgame.front.gui.components.common.graph.node.NodeId
import edu.agh.susgame.front.gui.components.common.graph.node.Router
import edu.agh.susgame.front.gui.components.common.graph.node.Server
import edu.agh.susgame.front.gui.components.common.util.Coordinates
import edu.agh.susgame.front.gui.components.common.util.player.PlayerLobby
import edu.agh.susgame.front.gui.components.common.util.player.PlayerStatus
import edu.agh.susgame.front.service.interfaces.LobbyService


class LobbyManager(
    val lobbyService: LobbyService,
    var id: LobbyId? = null,
    var name: String? = null,
    var maxNumOfPlayers: Int? = null,
    var gameTime: Int? = null,
    var playersMap: SnapshotStateMap<PlayerId, PlayerLobby> = mutableStateMapOf(),
    var playersRest: MutableMap<PlayerId, PlayerREST> = mutableMapOf(),
    var localId: PlayerId? = PlayerId(5),
    var gameManager: MutableState<GameManager?> = mutableStateOf(null)

) {
    fun updateFromRest(lobby: Lobby) {
        this.id = lobby.id
        this.name = lobby.name
        this.maxNumOfPlayers = lobby.maxNumOfPlayers
        this.gameTime = lobby.gameTime
        lobby.playersWaiting.forEach() {
            addPlayerRest(it)
        }
    }

    fun getPlayerStatus(id: PlayerId) = playersMap[id]?.status?.value

    fun addPlayerRest(player: PlayerREST) {
        playersMap[player.id] = PlayerLobby(
            name = player.nickname,
            status = mutableStateOf(PlayerStatus.NOT_READY)
        )
        playersRest[player.id] = player
    }

    fun getHowManyPlayersInLobby() = playersMap.size

    fun setId(id: PlayerId) {
        this.localId = id
    }

    fun updatePlayerStatus(id: PlayerId, status: PlayerStatus) {
        playersMap[id]?.status?.value = status
    }

    fun deletePlayer(playerId: PlayerId) {
        playersMap.remove(playerId)
        playersRest.remove(playerId)
    }

    fun getMapFromServer() {
        this.id?.let { id ->
            lobbyService.getGameMap(id)
                .thenApply { result -> parseMap(result) }
        }
    }

    private fun parseMap(gameMapDTO: GameMapDTO?) {
        if (gameMapDTO == null) {
            println("GameMapDTO is null")
            return
        }

        println("Parsing GameMapDTO: $gameMapDTO")

        val nodes = mutableListOf<Node>()

        gameMapDTO.server.let { server ->
            nodes.add(
                Server(
                    id = NodeId(server.id),
                    name = "S${server.id}",
                    position = Coordinates(server.coordinates.x, server.coordinates.y),
                    packetsToWin = 300
                )
            )
        }

        gameMapDTO.hosts.forEach { host ->
            nodes.add(
                Host(
                    id = NodeId(host.id),
                    name = "H${host.id}",
                    position = Coordinates(host.coordinates.x, host.coordinates.y),
                    playerId = PlayerId(0)
                )
            )
        }

        gameMapDTO.routers.forEach { router ->
            nodes.add(
                Router(
                    id = NodeId(router.id),
                    name = "R${router.id}",
                    position = Coordinates(router.coordinates.x, router.coordinates.y),
                    bufferSize = router.bufferSize
                )
            )
        }

        val edges = gameMapDTO.edges.mapIndexed { index, edgeDTO ->
            Edge(
                id = EdgeId(index),
                firstNodeId = NodeId(edgeDTO.from),
                secondNodeId = NodeId(edgeDTO.to),
                bandwidth = edgeDTO.weight
            )
        }

        val mapSize = Coordinates(1000, 1000)

        val serverId = gameMapDTO.server.id.let { NodeId(it) }

        gameManager.value = GameManager.fromLists(
            nodes = nodes,
            edges = edges,
            players = playersRest.values.toList(),
            serverId = serverId,
            mapSize = mapSize
        )

    }
}