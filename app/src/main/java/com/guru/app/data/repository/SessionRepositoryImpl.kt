package com.guru.app.data.repository

import com.guru.app.data.database.dao.FocusSessionDao
import com.guru.app.data.database.entity.FocusSessionEntity
import com.guru.app.domain.model.FocusSession
import com.guru.app.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionDao: FocusSessionDao
) : SessionRepository {
    override fun getAllSessions(): Flow<List<FocusSession>> {
        return sessionDao.getAllSessions().map { entities -> entities.map { it.toDomain() } }
    }

    override fun getTotalFocusSeconds(): Flow<Int> {
        return sessionDao.getTotalFocusSeconds().map { it ?: 0 }
    }

    override fun getTodayFocusSeconds(): Flow<Int> {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDay = calendar.timeInMillis
        return sessionDao.getTodayFocusSeconds(startOfDay).map { it ?: 0 }
    }

    override suspend fun saveSession(session: FocusSession): Long {
        return sessionDao.insertSession(FocusSessionEntity.fromDomain(session))
    }

    override suspend fun clearHistory() {
        sessionDao.clearAllSessions()
    }
}
