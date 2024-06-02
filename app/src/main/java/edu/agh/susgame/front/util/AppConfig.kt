package edu.agh.susgame.front.util

import kotlin.time.Duration

enum class ProviderType {
    MockLocal,
    Web,
}


interface AppConfig {
    val providers: ProviderType

    class WebConfig(
        val protocol: String,
        /**
         * NOTE: This value must be changed in `app/src/main/res/xml/network_security_config.xml` as well
         */
        val domain: String,
        val port: Int,

        val timeout: Duration,
    )

    val webConfig: WebConfig
}
