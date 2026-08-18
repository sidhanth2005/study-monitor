package com.guru.app.domain.model

data class UserProfile(
    val username: String = "Focus Master",
    val email: String = "user@guru.app",
    val dailyGoalMinutes: Int = 120,
    val currentXp: Int = 350,
    val currentLevel: Int = 3,
    val currentStreakDays: Int = 5,
    val totalFocusMinutes: Int = 480,
    val completedSessionsCount: Int = 16,
    val studyTarget: String = "Computer Science / Exam Prep"
) {
    val xpForNextLevel: Int
        get() = currentLevel * 250

    val levelProgressRatio: Float
        get() = (currentXp % 250).toFloat() / 250f
}
