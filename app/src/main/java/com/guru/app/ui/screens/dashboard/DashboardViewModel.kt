package com.guru.app.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guru.app.domain.model.Task
import com.guru.app.domain.model.UserProfile
import com.guru.app.domain.repository.AuthRepository
import com.guru.app.domain.repository.SessionRepository
import com.guru.app.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val userProfile: UserProfile = UserProfile(),
    val todayFocusSeconds: Int = 0,
    val pendingTasks: List<Task> = emptyList()
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        authRepository.getUserProfile(),
        sessionRepository.getTodayFocusSeconds(),
        taskRepository.getPendingTasks()
    ) { profile, todaySeconds, tasks ->
        DashboardUiState(
            userProfile = profile,
            todayFocusSeconds = todaySeconds,
            pendingTasks = tasks
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }
}
