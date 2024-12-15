package edu.agh.susgame.front.gui.components.common.util

import androidx.compose.runtime.mutableIntStateOf
import edu.agh.susgame.dto.rest.model.GameMapDTO
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.front.gui.components.common.graph.edge.Edge
import edu.agh.susgame.front.gui.components.common.graph.edge.EdgeId
import edu.agh.susgame.front.gui.components.common.graph.node.Host
import edu.agh.susgame.front.gui.components.common.graph.node.Node
import edu.agh.susgame.front.gui.components.common.graph.node.NodeId
import edu.agh.susgame.front.gui.components.common.graph.node.Router
import edu.agh.susgame.front.gui.components.common.graph.node.Server
import edu.agh.susgame.front.gui.components.common.util.player.PlayerLobby
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.service.interfaces.GameService

object ParserDTO {
    fun gameMapDtoToGameManager(
        gameService: GameService,
        gameMapDTO: GameMapDTO,
        localPlayerId: PlayerId,
        players: Map<PlayerId, PlayerLobby>,
    ): GameManager {
        println("Parsing GameMapDTO: $gameMapDTO")
        val nodes = mutableListOf<Node>()

        gameMapDTO.server.let { server ->
            nodes.add(
                Server(
                    id = NodeId(server.id),
                    name = "S${server.id}",
                    position = Coordinates(server.coordinates.x, server.coordinates.y),
                    packetsToWin = gameMapDTO.gameGoal
                )
            )
        }

        gameMapDTO.hosts.forEach { host ->
            nodes.add(
                Host(
                    id = NodeId(host.id),
                    name = players[PlayerId(host.playerId)]?.name?.value ?: "",
                    position = Coordinates(host.coordinates.x, host.coordinates.y),
                    playerId = PlayerId(host.playerId)
                )
            )
        }

        gameMapDTO.routers.forEach { router ->
            nodes.add(
                Router(
                    id = NodeId(router.id),
                    name = "R${router.id}",
                    position = Coordinates(router.coordinates.x, router.coordinates.y),
                    bufferSize = mutableIntStateOf(router.bufferSize)
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

        val gameManager =
            GameManager(
                nodesList = nodes,
                edgesList = edges,
                playersById = players,
                serverId = serverId,
                mapSize = mapSize,
                localPlayerId = localPlayerId,
                packetsToWin = gameMapDTO.gameGoal,
                criticalBufferOverheatLevel = gameMapDTO.criticalBufferOverheatLevel,
                gameService = gameService,
            )

        return gameManager
    }
}