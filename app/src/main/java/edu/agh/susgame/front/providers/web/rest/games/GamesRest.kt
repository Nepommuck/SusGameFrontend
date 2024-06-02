package edu.agh.susgame.front.providers.web.rest.games

import com.google.gson.Gson
import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.providers.web.rest.AbstractRest
import edu.agh.susgame.front.providers.web.rest.games.model.CreateGameApiResult
import edu.agh.susgame.front.providers.web.rest.games.model.GameCreationApiResponse
import edu.agh.susgame.front.providers.web.rest.games.model.GameCreationRequest
import edu.agh.susgame.front.providers.web.rest.games.model.GetAllGamesApiResult
import edu.agh.susgame.front.providers.web.rest.model.LobbyApi
import edu.agh.susgame.front.util.AppConfig.WebConfig
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.HttpURLConnection
import java.util.concurrent.CompletableFuture

class GamesRest(webConfig: WebConfig) : AbstractRest(webConfig, endpointName = "games") {
    fun getAllGames(): CompletableFuture<GetAllGamesApiResult> = CompletableFuture.supplyAsync {
        val request = Request.Builder()
            .url(baseUrl)
            .get()
            .build()

        val response = httpClient.newCall(request)
            .execute()

        if (!response.isSuccessful)
            GetAllGamesApiResult.Error
        else {
            val lobbies = Gson().fromJson(
                response.body?.string(),
                Array<LobbyApi>::class.java,
            ).toList()

            GetAllGamesApiResult.Success(lobbies)
        }
    }

    fun createGame(
        gameName: String,
        maxNumberOfPlayers: Int,
        gamePin: String? = null,
    ): CompletableFuture<CreateGameApiResult> = CompletableFuture.supplyAsync {
        val gameCreationRequest = GameCreationRequest(gameName, maxNumberOfPlayers, gamePin)

        val body = Gson().toJson(gameCreationRequest)
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(baseUrl)
            .post(body)
            .build()

        val response = httpClient.newCall(request)
            .execute()

        when (response.code) {
            HttpURLConnection.HTTP_CONFLICT ->
                CreateGameApiResult.NameAlreadyExists

            HttpURLConnection.HTTP_CREATED -> {
                val gameCreationApiResponse = Gson().fromJson(
                    response.body?.string(),
                    GameCreationApiResponse::class.java,
                )
                CreateGameApiResult.Success(
                    createdLobbyId = LobbyId(gameCreationApiResponse.gameId),
                )
            }

            else ->
                CreateGameApiResult.OtherError
        }
    }
}
