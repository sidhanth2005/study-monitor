package com.guru.app.domain.repository

import com.guru.app.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getUserProfile(): Flow<UserProfile>
    suspend fun login(email: String, pass: String): Boolean
    suspend fun signup(name: String, email: String, pass: String): Boolean
    suspend fun loginAsGuest(): Boolean
    suspend fun logout()
    suspend fun updateProfile(profile: UserProfile)
}
