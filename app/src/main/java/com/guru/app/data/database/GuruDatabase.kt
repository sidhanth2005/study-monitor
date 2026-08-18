package com.guru.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.guru.app.data.database.dao.FocusSessionDao
import com.guru.app.data.database.dao.TaskDao
import com.guru.app.data.database.dao.UserProfileDao
import com.guru.app.data.database.entity.FocusSessionEntity
import com.guru.app.data.database.entity.TaskEntity
import com.guru.app.data.database.entity.UserProfileEntity

@Database(
    entities = [
        TaskEntity::class,
        FocusSessionEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GuruDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun focusSessionDao(): FocusSessionDao
    abstract fun userProfileDao(): UserProfileDao
}
