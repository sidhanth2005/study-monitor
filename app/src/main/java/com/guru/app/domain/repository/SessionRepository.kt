package com.guru.app.domain.repository

import com.guru.app.domain.model.FocusSession
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getAllSessions(): Flow<List<FocusSession>>
    fun getTotalFocusSeconds(): Flow<Int>
    fun getTodayFocusSeconds(): Flow<Int>
    suspend fun saveSession(session: FocusSession): Long
    suspend fun clearHistory()
}
