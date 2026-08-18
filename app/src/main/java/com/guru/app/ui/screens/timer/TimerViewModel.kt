package com.guru.app.ui.screens.timer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guru.app.core.utils.NotificationHelper
import com.guru.app.domain.model.FocusSession
import com.guru.app.domain.model.TimerMode
import com.guru.app.domain.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TimerUiState(
    val selectedMode: TimerMode = TimerMode.POMODORO,
    val targetSeconds: Int = 25 * 60,
    val remainingSeconds: Int = 25 * 60,
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val isBreak: Boolean = false,
    val isFullscreenMode: Boolean = false,
    val isSessionCompleted: Boolean = false,
    val earnedXp: Int = 0
)

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun selectMode(mode: TimerMode) {
        timerJob?.cancel()
        val seconds = when (mode) {
            TimerMode.POMODORO -> 25 * 60
            TimerMode.COUNTDOWN -> 30 * 60
            TimerMode.DEEP_WORK -> 50 * 60
            TimerMode.CUSTOM -> 15 * 60
            TimerMode.STOPWATCH -> 0
        }
        _uiState.value = TimerUiState(
            selectedMode = mode,
            targetSeconds = seconds,
            remainingSeconds = seconds,
            isRunning = false
        )
    }

    fun startTimer() {
        if (_uiState.value.isRunning) return
        _uiState.value = _uiState.value.copy(isRunning = true, isPaused = false)

        timerJob = viewModelScope.launch {
            while (_uiState.value.isRunning) {
                delay(1000)
                val currentState = _uiState.value
                if (currentState.selectedMode == TimerMode.STOPWATCH) {
                    _uiState.value = currentState.copy(
                        remainingSeconds = currentState.remainingSeconds + 1,
                        targetSeconds = currentState.remainingSeconds + 1
                    )
                } else {
                    if (currentState.remainingSeconds > 1) {
                        _uiState.value = currentState.copy(
                            remainingSeconds = currentState.remainingSeconds - 1
                        )
                    } else {
                        // Timer completed!
                        onTimerFinished()
                        break
                    }
                }
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(isRunning = false, isPaused = true)
    }

    fun resetTimer() {
        timerJob?.cancel()
        val defaultSecs = when (_uiState.value.selectedMode) {
            TimerMode.POMODORO -> 25 * 60
            TimerMode.COUNTDOWN -> 30 * 60
            TimerMode.DEEP_WORK -> 50 * 60
            TimerMode.CUSTOM -> 15 * 60
            TimerMode.STOPWATCH -> 0
        }
        _uiState.value = _uiState.value.copy(
            remainingSeconds = defaultSecs,
            targetSeconds = defaultSecs,
            isRunning = false,
            isPaused = false
        )
    }

    fun toggleFullscreen() {
        _uiState.value = _uiState.value.copy(isFullscreenMode = !_uiState.value.isFullscreenMode)
    }

    private fun onTimerFinished() {
        timerJob?.cancel()
        val state = _uiState.value
        val duration = state.targetSeconds
        val xp = (duration / 60) * 2 // 2 XP per minute focused
        
        _uiState.value = state.copy(
            isRunning = false,
            isSessionCompleted = true,
            earnedXp = xp
        )

        NotificationHelper.showFocusCompleteNotification(
            context,
            state.selectedMode.name,
            duration / 60,
            xp
        )

        viewModelScope.launch {
            sessionRepository.saveSession(
                FocusSession(
                    mode = state.selectedMode,
                    durationSeconds = duration,
                    xpEarned = xp
                )
            )
        }
    }

    fun dismissSessionCompleteDialog() {
        _uiState.value = _uiState.value.copy(isSessionCompleted = false)
        resetTimer()
    }
}
