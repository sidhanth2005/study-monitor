package com.guru.app.domain.model

enum class TimerMode {
    POMODORO,
    COUNTDOWN,
    STOPWATCH,
    CUSTOM,
    DEEP_WORK
}

data class FocusSession(
    val id: Long = 0,
    val mode: TimerMode,
    val durationSeconds: Int,
    val xpEarned: Int,
    val dateTimestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)
