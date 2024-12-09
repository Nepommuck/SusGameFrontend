package edu.agh.susgame.front.gui.components.common.theme

import edu.agh.susgame.R


object Animations {
    data class AnimationFrames(val frames: List<Int>, val frameRate: Long)
    data class AnimationFramesString(val frames: List<String>, val frameRate: Long)

    val LOADING_ANIMATION = AnimationFrames(
        frames = generateFrames("i", 0..21),
        frameRate = 50
    )

    val TOKEN_ANIMATION = AnimationFrames(
        frames = generateFrames("token", 0..6),
        frameRate = 100
    )

    val HOURGLASS_ANIMATION = AnimationFramesString(
        frames = listOf("/", "-", "\\", "|"),
        frameRate = 300
    )

    private fun generateFrames(resourcePrefix: String, range: IntRange): List<Int> {
        return range.map { id ->
            val resourceName = "$resourcePrefix$id"
            val resourceId = R.drawable::class.java.getField(resourceName).getInt(null)
            resourceId
        }
    }
}