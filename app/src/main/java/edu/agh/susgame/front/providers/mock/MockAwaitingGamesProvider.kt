package edu.agh.susgame.front.providers.mock

import edu.agh.susgame.front.model.game.AwaitingGame
import edu.agh.susgame.front.model.game.GameId
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider

class MockAwaitingGamesProvider : AwaitingGamesProvider {
    private var state = mutableListOf(
        AwaitingGame(
            id = GameId(1),
            name = "Mafia",
            playersWaiting = listOf("John", "Terry"),
        ),
        AwaitingGame(
            id = GameId(2),
            name = "Amogus",
            playersWaiting = listOf("Michał", "Maciej", "Mateusz"),
        ),
        AwaitingGame(
            id = GameId(4),
            name = "Jakieś serwery na kiju",
            playersWaiting = listOf("Włodzimierz"),
        ),
        AwaitingGame(
            id = GameId(5),
            name = "ee",
            playersWaiting = listOf("Włodzimierz"),
        ),
    )

    override fun getAll(): MutableList<AwaitingGame> = state

    override fun getById(gameId: GameId): AwaitingGame? =
        this.getAll()
            .find { it.id == gameId }

    override fun join(id: GameId) {
        state = state.map {
            if (it.id == id)
                it.copy(playersWaiting = it.playersWaiting + "You")
            else
                it
        }.toMutableList()
    }
}