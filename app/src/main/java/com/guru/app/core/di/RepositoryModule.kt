package com.guru.app.core.di

import com.guru.app.data.repository.AuthRepositoryImpl
import com.guru.app.data.repository.SessionRepositoryImpl
import com.guru.app.data.repository.TaskRepositoryImpl
import com.guru.app.domain.repository.AuthRepository
import com.guru.app.domain.repository.SessionRepository
import com.guru.app.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(impl: SessionRepositoryImpl): SessionRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
