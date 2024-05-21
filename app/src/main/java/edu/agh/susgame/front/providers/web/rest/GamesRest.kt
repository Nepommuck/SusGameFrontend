package edu.agh.susgame.front.providers.web.rest

import edu.agh.susgame.front.providers.web.WebConfig
import okhttp3.Request
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
}
