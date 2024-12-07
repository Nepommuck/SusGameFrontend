package edu.agh.susgame.front.managers

import edu.agh.susgame.R


object ResourceManager {
    data class AnimationFrames(val frames: List<Int>, val frameRate: Long)
    data class AnimationFramesString(val frames: List<String>, val frameRate: Long)

    val LOADING_ANIMATION = AnimationFrames(
        frames = listOf(
            R.drawable.i0,
            R.drawable.i1,
            R.drawable.i2,
            R.drawable.i3,
            R.drawable.i4,
            R.drawable.i5,
            R.drawable.i6,
            R.drawable.i7,
            R.drawable.i8,
            R.drawable.i9,
            R.drawable.i10,
            R.drawable.i11,
            R.drawable.i12,
            R.drawable.i13,
            R.drawable.i14,
            R.drawable.i15,
            R.drawable.i16,
            R.drawable.i17,
            R.drawable.i18,
            R.drawable.i19,
            R.drawable.i20,
            R.drawable.i21,
        ), frameRate = 24
    )

    val TOKEN_ANIMATION = AnimationFrames(
        frames = listOf(
            R.drawable.token0,
            R.drawable.token1,
            R.drawable.token2,
            R.drawable.token3,
            R.drawable.token4,
            R.drawable.token5,
            R.drawable.token6,
        ), frameRate = 300
    )
    val HOURGLASS_ANIMATION = AnimationFramesString(
        frames = listOf("/", "-", "\\", "|"),
        frameRate = 300
    )


}