package edu.agh.susgame.front.service.web.rest.games

import com.google.gson.Gson
import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.service.web.rest.AbstractRest
import edu.agh.susgame.front.service.web.rest.games.model.CreateGameApiResult
import edu.agh.susgame.front.service.web.rest.games.model.GameCreationApiResponse
import edu.agh.susgame.front.service.web.rest.games.model.GameCreationRequest
import edu.agh.susgame.front.service.web.rest.games.model.GetAllGamesApiResult
import edu.agh.susgame.front.service.web.rest.games.model.GetGameApiResult
import edu.agh.susgame.front.service.web.rest.model.LobbyApi
import edu.agh.susgame.front.util.AppConfig.WebConfig
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.HttpURLConnection
import java.util.concurrent.CompletableFuture

class GamesRest(webConfig: WebConfig) : AbstractRest(webConfig, endpointName = "games") {
    fun getAllGames(): CompletableFuture<GetAllGamesApiResult> = CompletableFuture.supplyAsync {
        val request = Request.Builder()
            .get()
            .url(baseUrlBuilder().build())
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

    fun getGame(gameId: LobbyId): CompletableFuture<GetGameApiResult> =
        CompletableFuture.supplyAsync {
            val request = Request.Builder()
                .get()
                .url(
                    baseUrlBuilder()
                        .addPathSegment(gameId.value.toString())
                        .build()
                ).build()

            val response = httpClient.newCall(request)
                .execute()

            when (response.code) {
                HttpURLConnection.HTTP_NOT_FOUND -> GetGameApiResult.DoesNotExist

                HttpURLConnection.HTTP_OK -> {
                    val lobby = Gson().fromJson(
                        response.body?.string(),
                        LobbyApi::class.java,
                    )
                    GetGameApiResult.Success(lobby)
                }

                else -> GetGameApiResult.OtherError
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
            .post(body)
            .url(baseUrlBuilder().build())
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
