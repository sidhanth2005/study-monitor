package com.guru.app.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guru.app.data.datastore.UserPreferencesRepository
import com.guru.app.ui.navigation.ScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination

    init {
        viewModelScope.launch {
            val isOnboarded = userPreferencesRepository.isOnboardingCompleted.first()
            val isLoggedIn = userPreferencesRepository.isLoggedIn.first()

            _startDestination.value = when {
                !isOnboarded -> ScreenRoute.Onboarding.route
                !isLoggedIn -> ScreenRoute.Login.route
                else -> ScreenRoute.Dashboard.route
            }
        }
    }
}
