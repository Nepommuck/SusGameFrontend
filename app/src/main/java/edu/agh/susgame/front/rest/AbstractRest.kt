package edu.agh.susgame.front.rest

import edu.agh.susgame.front.util.AppConfig.WebConfig
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import kotlin.time.toJavaDuration

abstract class AbstractRest(private val webConfig: WebConfig, val endpointName: String) {
    val httpClient = OkHttpClient.Builder()
        // TODO GAME-58 It doesn't work
        .connectTimeout(webConfig.timeout.toJavaDuration())
        .readTimeout(webConfig.timeout.toJavaDuration())
        .writeTimeout(webConfig.timeout.toJavaDuration())
        .build()

    fun baseUrlBuilder(): HttpUrl.Builder = HttpUrl.Builder()
        .scheme(webConfig.protocol)
        .host(webConfig.domain)
        .port(webConfig.port)
        .addPathSegment(endpointName)
}
