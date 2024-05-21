package edu.agh.susgame.front.providers.web

import com.google.gson.Gson
import edu.agh.susgame.front.model.PlayerNickname
import edu.agh.susgame.front.model.game.AwaitingGame
import edu.agh.susgame.front.model.game.GameId
import edu.agh.susgame.front.providers.mock.MockAwaitingGamesProvider
import edu.agh.susgame.front.providers.web.rest.GamesRest
import edu.agh.susgame.front.providers.web.rest.model.AwaitingGameRest
import java.util.concurrent.CompletableFuture

//class WebAwaitingGamesProvider(private val gamesRest: GamesRest) : AwaitingGamesProvider {
class WebAwaitingGamesProvider(private val gamesRest: GamesRest) : MockAwaitingGamesProvider() {
    override fun getAll(): CompletableFuture<List<AwaitingGame>> =
        gamesRest.getAllGames().thenApply { response ->

            Gson().fromJson(
                response.body?.string(),
                Array<AwaitingGameRest>::class.java,
            ).toList()
                .map {
                    awaitingGameFromRest(it)
                }
        }

//  TODO GAME-24
//    override fun getById(gameId: GameId): CompletableFuture<AwaitingGame?> {
//        TODO("Not yet implemented")
//    }
//
//    override fun createNewGame(
//        gameName: String,
//        gamePin: String,
//        numOfPlayers: Int,
//        gameTime: Int
//    ): CompletableFuture<GameId> {
//        TODO("Not yet implemented")
//    }

    companion object {
        private fun awaitingGameFromRest(awaitingGameRest: AwaitingGameRest): AwaitingGame =
            AwaitingGame(
                id = GameId(awaitingGameRest.id),
                name = awaitingGameRest.name,
                maxNumOfPlayers = awaitingGameRest.maxNumberOfPlayers,
                gameTime = 10,
                gamePin = null,
                playersWaiting = awaitingGameRest.players.map { PlayerNickname(it) },
            )
    }
}
