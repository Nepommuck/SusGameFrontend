package edu.agh.susgame.front.providers.interfaces

import edu.agh.susgame.front.model.ServerMapState
import edu.agh.susgame.front.model.game.GameId
import java.util.concurrent.CompletableFuture

interface ServerMapProvider {
    fun getServerMapState(gameId: GameId): CompletableFuture<ServerMapState>
}
