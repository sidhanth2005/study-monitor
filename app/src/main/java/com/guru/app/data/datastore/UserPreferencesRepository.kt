package com.guru.app.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "guru_user_prefs")

class UserPreferencesRepository(private val context: Context) {

    private object PrefKeys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_NAME = stringPreferencesKey("user_name")
        val THEME_MODE = stringPreferencesKey("theme_mode") // SYSTEM, LIGHT, DARK, AMOLED
        val DAILY_GOAL_MINUTES = intPreferencesKey("daily_goal_minutes")
        val POMODORO_MINUTES = intPreferencesKey("pomodoro_minutes")
        val SHORT_BREAK_MINUTES = intPreferencesKey("short_break_minutes")
        val LONG_BREAK_MINUTES = intPreferencesKey("long_break_minutes")
        val SOUND_VOLUME = floatPreferencesKey("sound_volume")
    }

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[PrefKeys.ONBOARDING_COMPLETED] ?: false
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[PrefKeys.IS_LOGGED_IN] ?: false
    }

    val userEmail: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PrefKeys.USER_EMAIL] ?: "guest@guru.app"
    }

    val userName: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PrefKeys.USER_NAME] ?: "Focus Master"
    }

    val themeMode: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PrefKeys.THEME_MODE] ?: "AMOLED"
    }

    val dailyGoalMinutes: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[PrefKeys.DAILY_GOAL_MINUTES] ?: 120
    }

    val pomodoroMinutes: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[PrefKeys.POMODORO_MINUTES] ?: 25
    }

    val shortBreakMinutes: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[PrefKeys.SHORT_BREAK_MINUTES] ?: 5
    }

    val longBreakMinutes: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[PrefKeys.LONG_BREAK_MINUTES] ?: 15
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PrefKeys.ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun setLoggedIn(loggedIn: Boolean, email: String = "user@guru.app", name: String = "Focus Master") {
        context.dataStore.edit { prefs ->
            prefs[PrefKeys.IS_LOGGED_IN] = loggedIn
            prefs[PrefKeys.USER_EMAIL] = email
            prefs[PrefKeys.USER_NAME] = name
        }
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { prefs ->
            prefs[PrefKeys.THEME_MODE] = mode
        }
    }

    suspend fun setDailyGoalMinutes(minutes: Int) {
        context.dataStore.edit { prefs ->
            prefs[PrefKeys.DAILY_GOAL_MINUTES] = minutes
        }
    }

    suspend fun setTimerDurations(pomodoro: Int, shortBreak: Int, longBreak: Int) {
        context.dataStore.edit { prefs ->
            prefs[PrefKeys.POMODORO_MINUTES] = pomodoro
            prefs[PrefKeys.SHORT_BREAK_MINUTES] = shortBreak
            prefs[PrefKeys.LONG_BREAK_MINUTES] = longBreak
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs[PrefKeys.IS_LOGGED_IN] = false
            prefs[PrefKeys.USER_EMAIL] = "guest@guru.app"
            prefs[PrefKeys.USER_NAME] = "Guest User"
        }
    }
}
