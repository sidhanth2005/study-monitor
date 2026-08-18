package com.guru.app.core.di

import android.content.Context
import androidx.room.Room
import com.guru.app.data.database.GuruDatabase
import com.guru.app.data.database.dao.FocusSessionDao
import com.guru.app.data.database.dao.TaskDao
import com.guru.app.data.database.dao.UserProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGuruDatabase(@ApplicationContext context: Context): GuruDatabase {
        return Room.databaseBuilder(
            context,
            GuruDatabase::class.java,
            "guru_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideTaskDao(db: GuruDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideFocusSessionDao(db: GuruDatabase): FocusSessionDao = db.focusSessionDao()

    @Provides
    fun provideUserProfileDao(db: GuruDatabase): UserProfileDao = db.userProfileDao()
}
