package edu.agh.susgame.front.providers.interfaces


import edu.agh.susgame.front.model.PlayerId
import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.model.graph.GameGraph
import edu.agh.susgame.front.model.graph.NodeId
import java.util.concurrent.CompletableFuture

interface GameGraphProvider {
    fun getServerMapState(lobbyId: LobbyId): CompletableFuture<GameGraph>

    /**
     * Changes the path that player had chosen.
     * @param playerId
     */
    fun changePlayerPath(playerId: PlayerId, path: List<NodeId>)
}
