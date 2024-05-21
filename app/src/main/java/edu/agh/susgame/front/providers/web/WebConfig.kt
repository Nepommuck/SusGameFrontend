package edu.agh.susgame.front.providers.web

interface WebConfig {
    val backendBaseUrl: String
}

object LocalWebConfig : WebConfig {
    override val backendBaseUrl = "http://192.168.0.102:8080"
//    override val backendBaseUrl = "http://0.0.0.0:8080"
}
