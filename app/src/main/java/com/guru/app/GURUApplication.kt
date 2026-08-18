package com.guru.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GURUApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val focusChannel = NotificationChannel(
                CHANNEL_FOCUS,
                "Focus Timer",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for focus session timer and breaks"
            }

            val reminderChannel = NotificationChannel(
                CHANNEL_REMINDER,
                "Daily Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily study and focus task reminders"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(focusChannel)
            notificationManager?.createNotificationChannel(reminderChannel)
        }
    }

    companion object {
        const val CHANNEL_FOCUS = "channel_guru_focus"
        const val CHANNEL_REMINDER = "channel_guru_reminder"
    }
}
