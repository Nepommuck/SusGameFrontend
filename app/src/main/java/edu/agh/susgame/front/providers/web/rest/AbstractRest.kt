package edu.agh.susgame.front.providers.web.rest

import edu.agh.susgame.front.providers.web.WebConfig
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient

abstract class AbstractRest(webConfig: WebConfig, val endpointName: String) {
    val httpClient = OkHttpClient()

    val baseUrl = "${webConfig.backendBaseUrl}/$endpointName".toHttpUrl()
}
