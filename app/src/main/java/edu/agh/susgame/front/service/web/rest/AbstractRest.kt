package edu.agh.susgame.front.service.web.rest

import edu.agh.susgame.front.config.utils.Configuration.WebConfig
import edu.agh.susgame.front.service.web.IpAddressProvider
import okhttp3.HttpUrl
import okhttp3.OkHttpClient

abstract class AbstractRest(val endpointName: String) {
    protected abstract val ipAddressProvider: IpAddressProvider
    protected abstract val webConfig: WebConfig

    val httpClient = OkHttpClient.Builder()
        // TODO GAME-58 It doesn't work
        //    .connectTimeout(webConfig.timeout.toJavaDuration())
        //    .readTimeout(webConfig.timeout.toJavaDuration())
        //    .writeTimeout(webConfig.timeout.toJavaDuration())
        .build()

    fun baseUrlBuilder(): HttpUrl.Builder {
        val ipAddress = ipAddressProvider.getCurrentIpAddress() ?: throw IllegalStateException(
            "A HTTP url builder was tried to be constructed, but IP address is currently not defined"
        )
        return HttpUrl.Builder()
            .scheme(webConfig.protocol)
            .host(ipAddress)
            .port(webConfig.port)
            .addPathSegment(endpointName)
    }
}
