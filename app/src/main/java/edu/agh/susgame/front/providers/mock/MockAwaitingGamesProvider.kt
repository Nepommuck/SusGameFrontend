package edu.agh.susgame.front.providers.mock

import edu.agh.susgame.front.model.PlayerNickname
import edu.agh.susgame.front.model.game.AwaitingGame
import edu.agh.susgame.front.model.game.GameId
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider

class MockAwaitingGamesProvider : AwaitingGamesProvider {
    private var state = mutableListOf(
        AwaitingGame(
            id = GameId(1),
            name = "Mafia",
            playersWaiting = listOf("John", "Terry").map { PlayerNickname(it) },
        ),
        AwaitingGame(
            id = GameId(2),
            name = "Amogus",
            playersWaiting = listOf("Michał", "Maciej", "Mateusz").map { PlayerNickname(it) },
        ),
        AwaitingGame(
            id = GameId(4),
            name = "Jakieś serwery na kiju",
            playersWaiting = listOf("Włodzimierz").map { PlayerNickname(it) },
        ),
        AwaitingGame(
            id = GameId(5),
            name = "Podstawówka",
            playersWaiting = listOf("Jan", "Paweł").map { PlayerNickname(it) },
        ),
    )

    override fun getAll(): MutableList<AwaitingGame> = state

    override fun getById(gameId: GameId): AwaitingGame? =
        this.getAll()
            .find { it.id == gameId }

    override fun join(id: GameId, playerNickname: PlayerNickname) {
        state = state.map {
            if (it.id == id)
                it.copy(playersWaiting = it.playersWaiting + playerNickname)
            else
                it
        }.toMutableList()
    }

    override fun leave(id: GameId, playerNickname: PlayerNickname) {
        state = state.map { game ->
            if (game.id == id)
                game.copy(playersWaiting = game.playersWaiting.filter { it != playerNickname })
            else
                game
        }.toMutableList()
    }
}