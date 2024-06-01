package edu.agh.susgame.front.providers.web.rest

import com.google.gson.Gson
import edu.agh.susgame.front.providers.web.GameCreationRequest
import edu.agh.susgame.front.providers.web.WebConfig
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.util.concurrent.CompletableFuture

class GamesRest(webConfig: WebConfig) : AbstractRest(webConfig, endpointName = "games") {
    fun getAllGames(): CompletableFuture<Response> =
        CompletableFuture.supplyAsync {
            val request = Request.Builder()
                .url(baseUrl)
                .get()
                .build()
            println(baseUrl)

            httpClient.newCall(request).execute()
        }

    fun addGame(gameRequest: GameCreationRequest) =
        CompletableFuture.supplyAsync {
            val body = Gson().toJson(gameRequest)
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(baseUrl)
                .post(body)
                .build()
            println(baseUrl)

            httpClient.newCall(request).execute()
        }
}
