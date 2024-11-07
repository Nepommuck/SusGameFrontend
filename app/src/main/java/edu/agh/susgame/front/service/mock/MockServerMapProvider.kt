package edu.agh.susgame.front.providers.mock

import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.rest.model.PlayerREST
import edu.agh.susgame.front.ui.graph.Edge
import edu.agh.susgame.front.ui.graph.EdgeId
import edu.agh.susgame.front.ui.graph.GameMapFront
import edu.agh.susgame.front.ui.graph.node.Host
import edu.agh.susgame.front.ui.graph.node.NodeId
import edu.agh.susgame.front.ui.graph.PathBuilder
import edu.agh.susgame.front.ui.graph.node.Router
import edu.agh.susgame.front.ui.graph.node.Server
import edu.agh.susgame.front.service.interfaces.ServerMapProvider
import edu.agh.susgame.front.ui.components.common.util.Coordinates
import java.util.concurrent.CompletableFuture

class MockServerMapProvider(mockDelayMs: Long? = null) : ServerMapProvider {
    private val delayMs = mockDelayMs ?: 0

    private val gameGraphState = createCustomMapState()

    override fun getServerMapState(
        lobbyId: LobbyId,
    ): CompletableFuture<GameMapFront> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)
            gameGraphState
        }

    override fun changePlayerPath(playerId: PlayerId, pathBuilder: PathBuilder) {
        // TODO Implement
    }

    /**
     * This function is only for testing, it shows logic behind creating game map
     */
    private fun createCustomMapState(): GameMapFront {

        val nodes = listOf(
            Router(NodeId(0), "R1", Coordinates(150, 150), 30),
            Router(NodeId(1), "R2", Coordinates(250, 150), 30),
            Router(NodeId(2), "R3", Coordinates(200, 25), 30),

            Host(NodeId(3), "H1", Coordinates(100, 275), PlayerId(0)),
            Host(NodeId(4), "H2", Coordinates(200, 275), PlayerId(1)),
            Host(NodeId(5), "H3", Coordinates(350, 250), PlayerId(2)),

            Server(NodeId(6), "S1", Coordinates(75, 50), 300),
        )
        val serverId = NodeId(6)

        val edges = listOf(
            Edge(EdgeId(0), NodeId(3), NodeId(0), 5),
            Edge(EdgeId(1), NodeId(4), NodeId(0), 5),
            Edge(EdgeId(2), NodeId(1), NodeId(5), 5),
            Edge(EdgeId(3), NodeId(0), NodeId(1), 5),
            Edge(EdgeId(4), NodeId(0), NodeId(2), 5),
            Edge(EdgeId(5), NodeId(0), NodeId(6), 5),
            Edge(EdgeId(6), NodeId(1), NodeId(2), 5),
            Edge(EdgeId(7), NodeId(2), NodeId(6), 5),
        )

        val players = listOf(
            PlayerREST(
                nickname = PlayerNickname("Player_0"),
                colorHex = 0xAF00FF00,
                id = PlayerId(0)
            ),
            PlayerREST(
                nickname = PlayerNickname("Player_1"),
                colorHex = 0xAFF000FF,
                id = PlayerId(1)
            ),
            PlayerREST(
                nickname = PlayerNickname("Player_2"),
                colorHex = 0xAFFF5733,
                id = PlayerId(2)
            ),
        )
        val mapSize = Coordinates(1000, 1000)

        val gameMapFrontState = GameMapFront.fromLists(
            nodes = nodes,
            edges = edges,
            players = players,
            serverId = serverId,
            mapSize = mapSize
        )

        return gameMapFrontState
    }
}
