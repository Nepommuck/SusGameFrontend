package edu.agh.susgame.front.providers.interfaces

import edu.agh.susgame.front.model.game.AwaitingGame
import edu.agh.susgame.front.model.game.GameId

interface AwaitingGamesProvider {
    fun getAll(): MutableList<AwaitingGame>

    fun getById(gameId: GameId): AwaitingGame?

    fun join(id: GameId)
}
