package com.guru.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.guru.app.domain.model.UserProfile

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val username: String,
    val email: String,
    val dailyGoalMinutes: Int,
    val currentXp: Int,
    val currentLevel: Int,
    val currentStreakDays: Int,
    val totalFocusMinutes: Int,
    val completedSessionsCount: Int,
    val studyTarget: String
) {
    fun toDomain(): UserProfile = UserProfile(
        username = username,
        email = email,
        dailyGoalMinutes = dailyGoalMinutes,
        currentXp = currentXp,
        currentLevel = currentLevel,
        currentStreakDays = currentStreakDays,
        totalFocusMinutes = totalFocusMinutes,
        completedSessionsCount = completedSessionsCount,
        studyTarget = studyTarget
    )

    companion object {
        fun fromDomain(profile: UserProfile): UserProfileEntity = UserProfileEntity(
            id = 1,
            username = profile.username,
            email = profile.email,
            dailyGoalMinutes = profile.dailyGoalMinutes,
            currentXp = profile.currentXp,
            currentLevel = profile.currentLevel,
            currentStreakDays = profile.currentStreakDays,
            totalFocusMinutes = profile.totalFocusMinutes,
            completedSessionsCount = profile.completedSessionsCount,
            studyTarget = profile.studyTarget
        )
    }
}
