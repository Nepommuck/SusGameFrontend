package edu.agh.susgame.front.service.interfaces


import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.front.model.graph.GameGraph
import edu.agh.susgame.front.model.graph.PathBuilder
import java.util.concurrent.CompletableFuture

/**
 * This Provider will soon be deleted. Map state will be served by `GameService`
 **/
interface ServerMapProvider {
    fun getServerMapState(lobbyId: LobbyId): CompletableFuture<GameGraph>
    fun changePlayerPath(playerId: PlayerId, pathBuilder: PathBuilder)
}
