package edu.agh.susgame.front.providers.interfaces

import edu.agh.susgame.front.model.AwaitingGame

interface AwaitingGamesProvider {
    fun getAll(): MutableList<AwaitingGame>

    fun getById(gameId: AwaitingGame.AwaitingGameId): AwaitingGame?

    fun join(id: AwaitingGame.AwaitingGameId)
}
