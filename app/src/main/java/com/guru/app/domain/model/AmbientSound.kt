package com.guru.app.domain.model

enum class SoundType {
    RAIN,
    FOREST,
    OCEAN,
    CAFE,
    WHITE_NOISE,
    BROWN_NOISE,
    PINK_NOISE
}

data class AmbientSound(
    val id: String,
    val name: String,
    val type: SoundType,
    val iconName: String,
    val isPlaying: Boolean = false,
    val volume: Float = 0.7f
)
