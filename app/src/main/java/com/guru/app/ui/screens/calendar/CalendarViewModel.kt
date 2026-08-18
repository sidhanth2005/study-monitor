package com.guru.app.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guru.app.domain.model.FocusSession
import com.guru.app.domain.model.Task
import com.guru.app.domain.repository.SessionRepository
import com.guru.app.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.util.Calendar
import javax.inject.Inject

enum class CalendarViewMode { MONTHLY, WEEKLY, DAILY }

data class CalendarUiState(
    val selectedMode: CalendarViewMode = CalendarViewMode.MONTHLY,
    val selectedDayOfMonth: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
    val tasks: List<Task> = emptyList(),
    val sessions: List<FocusSession> = emptyList()
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    taskRepository: TaskRepository,
    sessionRepository: SessionRepository
) : ViewModel() {

    private val _viewMode = MutableStateFlow(CalendarViewMode.MONTHLY)
    val viewMode: StateFlow<CalendarViewMode> = _viewMode.asStateFlow()

    private val _selectedDay = MutableStateFlow(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
    val selectedDay: StateFlow<Int> = _selectedDay.asStateFlow()

    val uiState: StateFlow<CalendarUiState> = combine(
        _viewMode,
        _selectedDay,
        taskRepository.getAllTasks(),
        sessionRepository.getAllSessions()
    ) { mode, day, tasks, sessions ->
        CalendarUiState(
            selectedMode = mode,
            selectedDayOfMonth = day,
            tasks = tasks,
            sessions = sessions
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalendarUiState()
    )

    fun setViewMode(mode: CalendarViewMode) {
        _viewMode.value = mode
    }

    fun selectDay(day: Int) {
        _selectedDay.value = day
    }
}
