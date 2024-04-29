package edu.agh.susgame.front.providers.mock

import edu.agh.susgame.front.model.AwaitingGame
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider

class MockAwaitingGamesProvider : AwaitingGamesProvider {
    private var state = mutableListOf(
        AwaitingGame(
            id = AwaitingGame.AwaitingGameId(1),
            name = "Mafia",
            description = "Przychodzisz do mnie w dniu ślubu mej córki i chcesz, " +
                    "żebym zabijał za pieniądze",
            playersWaiting = 5,
        ),
        AwaitingGame(
            id = AwaitingGame.AwaitingGameId(2),
            name = "Amogus",
            description = "Sus",
            playersWaiting = 2,
        ),
        AwaitingGame(
            id = AwaitingGame.AwaitingGameId(4),
            name = "Jakieś serwery na kiju",
            description = "Sinus consinus daj Turek trzy minus",
            playersWaiting = 1,
        ),
    )

    override fun getAll(): MutableList<AwaitingGame> = state

    override fun getById(gameId: AwaitingGame.AwaitingGameId): AwaitingGame? =
        this.getAll()
            .find { it.id == gameId }

    override fun join(id: AwaitingGame.AwaitingGameId) {
        state.map {
            if (it.id == id)
                it.copy(playersWaiting = it.playersWaiting + 1)
            else
                it
        }
    }
}