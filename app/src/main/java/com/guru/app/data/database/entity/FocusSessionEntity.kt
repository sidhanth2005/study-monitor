package com.guru.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.guru.app.domain.model.FocusSession
import com.guru.app.domain.model.TimerMode

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mode: String,
    val durationSeconds: Int,
    val xpEarned: Int,
    val dateTimestamp: Long,
    val notes: String
) {
    fun toDomain(): FocusSession = FocusSession(
        id = id,
        mode = try { TimerMode.valueOf(mode) } catch (e: Exception) { TimerMode.POMODORO },
        durationSeconds = durationSeconds,
        xpEarned = xpEarned,
        dateTimestamp = dateTimestamp,
        notes = notes
    )

    companion object {
        fun fromDomain(session: FocusSession): FocusSessionEntity = FocusSessionEntity(
            id = session.id,
            mode = session.mode.name,
            durationSeconds = session.durationSeconds,
            xpEarned = session.xpEarned,
            dateTimestamp = session.dateTimestamp,
            notes = session.notes
        )
    }
}
