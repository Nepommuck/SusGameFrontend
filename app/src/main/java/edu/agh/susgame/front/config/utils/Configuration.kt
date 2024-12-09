package edu.agh.susgame.front.config.utils

import kotlin.time.Duration

interface Configuration {
    val providers: ProviderType

    class WebConfig(
        val protocol: String,
        val defaultIpAddress: String?,
        val port: Int,
    )

    val webConfig: WebConfig

    class GameConfig(
        val maxPinLength: Int,
        val playersPerGame: PlayersPerGame,
        val quizConfig: QuizConfig,
    ) {
        data class PlayersPerGame(
            val min: Int,
            val max: Int,
        )

        data class QuizConfig(
            val awaitAnswerGradeDuration: Duration,
            val loadNextQuestionCooldown: Duration,
        )
    }

    val gameConfig: GameConfig
}
