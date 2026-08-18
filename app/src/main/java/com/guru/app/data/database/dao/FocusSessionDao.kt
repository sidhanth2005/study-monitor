package com.guru.app.data.database.dao

import androidx.room.*
import com.guru.app.data.database.entity.FocusSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions ORDER BY dateTimestamp DESC")
    fun getAllSessions(): Flow<List<FocusSessionEntity>>

    @Query("SELECT SUM(durationSeconds) FROM focus_sessions")
    fun getTotalFocusSeconds(): Flow<Int?>

    @Query("SELECT SUM(durationSeconds) FROM focus_sessions WHERE dateTimestamp >= :startOfDayTimestamp")
    fun getTodayFocusSeconds(startOfDayTimestamp: Long): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: FocusSessionEntity): Long

    @Query("DELETE FROM focus_sessions")
    suspend fun clearAllSessions()
}
