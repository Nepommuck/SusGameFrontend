package edu.agh.susgame.front.providers.web.rest

import edu.agh.susgame.front.util.AppConfig.WebConfig
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import kotlin.time.toJavaDuration

abstract class AbstractRest(webConfig: WebConfig, val endpointName: String) {
    val httpClient = OkHttpClient.Builder()
        // TODO GAME-58 It doesn't work
        .connectTimeout(webConfig.timeout.toJavaDuration())
        .readTimeout(webConfig.timeout.toJavaDuration())
        .writeTimeout(webConfig.timeout.toJavaDuration())
        .build()

    val baseUrl = "${webConfig.fullUrl}/$endpointName".toHttpUrl()
}
